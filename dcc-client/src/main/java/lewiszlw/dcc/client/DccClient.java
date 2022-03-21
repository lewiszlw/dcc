package lewiszlw.dcc.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.io.Files;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lewiszlw.dcc.client.annotation.DccConfig;
import lewiszlw.dcc.client.listener.ConfigItemChangeListener;
import lewiszlw.dcc.client.listener.ConfigSpaceChangeListener;
import lewiszlw.dcc.client.listener.ItemChangeContext;
import lewiszlw.dcc.client.listener.SpaceChangeContext;
import lewiszlw.dcc.client.util.FileUtil;
import lewiszlw.dcc.iface.ConfigDubboService;
import lewiszlw.dcc.iface.constant.Env;
import lewiszlw.dcc.iface.response.ConfigDTO;
import lewiszlw.dcc.iface.util.ZkUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.dubbo.config.annotation.Reference;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-04-26
 */
@Slf4j
@NoArgsConstructor
public class DccClient {

    private static final String ZK_URL = "127.0.0.1:2181";
    private static final int SESSION_TIMEOUT_MS = 60000;
    private static final int CONNECTION_TIMEOUT_MS = 15000;

    @Getter @Setter
    private String application;
    @Getter @Setter
    private Env env;
    @Getter @Setter
    private String scanBasePackages = ".";
    @Getter @Setter
    private String cacheFilePath = "/opt/dcc/cache";
    @Getter @Setter
    private long period = 180;

    private Reflections reflections;
    private File cacheFile;
    private ObjectMapper objectMapper;
    private ScheduledExecutorService scheduledExecutorService;
    private volatile Map<String, String> configCache;
    private Map<String, Set<Field>> keyToFieldsMap;
    private List<ConfigSpaceChangeListener> configSpaceChangeListeners;
    private Map<String, List<ConfigItemChangeListener>> keyToConfigItemChangeListenersMap;
    private CuratorFramework zkClient;

    @Reference(version = "1.0.0", check = false)
    private ConfigDubboService configDubboService;

    /**
     * ==================methods for users=======================
     */
    /**
     * 根据key获取配置
     */
    public String get(String key) {
        Preconditions.checkArgument(!StringUtils.isEmpty(key), "key is empty");
        return configCache.get(key);
    }

    public Map<String, String> getAll() {
        return configCache;
    }

    /**
     * spring bean init
     */
    private void init() throws Exception {
        // 初始化参数并检查
        initParamAndCheck();
        // 全量拉取配置到cache
        updateAndPersistCache(initLoadConfigs(), true);
        // 扫描注解，给字段注入配置value
        scanAnnotation();
        // 初始化zk，通过在父节点添加watcher来监听所有配置变化
        initZkClientAndRegisterListener();
        // 启动定时任务
        startScheduledTask();
    }

    /**
     * 初始化参数并检查
     */
    private void initParamAndCheck() throws IOException {
        Preconditions.checkArgument(!StringUtils.isEmpty(application), "application is empty");
        Preconditions.checkArgument(env != null, "env is null");
        reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(scanBasePackages))
                .setScanners(new FieldAnnotationsScanner()));
        cacheFile = new File(cacheFilePath);
        FileUtil.createIfNotExists(cacheFile);
        objectMapper = new ObjectMapper();
        scheduledExecutorService = new ScheduledThreadPoolExecutor(1,
                new ThreadFactoryBuilder().setNameFormat("dcc-schedule-pool-%d").setDaemon(true).build());
        configCache = new ConcurrentHashMap<>(100);
        keyToFieldsMap = new HashMap<>();
        configSpaceChangeListeners = new ArrayList<>();
        keyToConfigItemChangeListenersMap = new HashMap<>();
    }

    /**
     * 启动时全量拉取或从缓存文件加载配置
     */
    private Map<String, String> initLoadConfigs() throws IOException {
        try {
            List<ConfigDTO> configDTOS = configDubboService.queryConfigs(application, env);
            log.debug("初始化从server端拉取配置数据：{}", objectMapper.writeValueAsString(configDTOS));
            if (CollectionUtils.isEmpty(configDTOS)) {
                log.warn("DccClient全量拉取配置为空，请确认是否存在异常");
            }
            return configDTOS.stream().collect(Collectors.toMap(ConfigDTO::getKey, ConfigDTO::getValue));
        } catch (Throwable th) {
            log.error("DccClient全量拉取配置异常，使用缓存文件加载配置，配置更新功能将无法使用", th);
            Map<String, String> configsFromCacheFile = objectMapper.readValue(cacheFile, Map.class);
            log.info("从本地文件拉取配置数据：{}", objectMapper.writeValueAsString(configsFromCacheFile));
            return configsFromCacheFile;
        }
    }

    /**
     * 从server端全量拉取配置
     */
    private List<ConfigDTO> pullFromServer() {
        try {
            List<ConfigDTO> configDTOS = configDubboService.queryConfigs(application, env);
            log.info("从server端全量拉取配置数据：{}", objectMapper.writeValueAsString(configDTOS));
            if (CollectionUtils.isEmpty(configDTOS)) {
                log.warn("DccClient全量拉取配置为空，请确认是否存在异常");
            }
            return configDTOS;
        } catch (Throwable th) {
            log.error("DccClient全量拉取配置异常", th);
            return Collections.emptyList();
        }
    }

    /**
     * 扫描注解并给字段设定值和监听器
     */
    private void scanAnnotation() {
        Set<Field> fields = reflections.getFieldsAnnotatedWith(DccConfig.class);
        for (Field field : fields) {
            DccConfig dccConfig = field.getAnnotation(DccConfig.class);
            String key = StringUtils.isEmpty(dccConfig.key()) ? field.getName(): dccConfig.key();

            // 保存 key与fields 映射关系
            Set<Field> fieldsUsingSameKey = keyToFieldsMap.getOrDefault(key, new HashSet<>());
            fieldsUsingSameKey.add(field);
            keyToFieldsMap.put(key, fieldsUsingSameKey);

            // 设置field值
            if (configCache.containsKey(key)) {
                setFieldValue(field, configCache.get(key));
            } else {
                log.error("key={} 未进行配置或未生效，请检查", key);
            }
        }
    }

    /**
     * 给 @DccConfig 注解的字段设置值
     */
    private void setFieldValue(Field field, String value) {
        try {
            field.setAccessible(true);
            if (String.class.equals(field.getType())) {
                field.set(field.getDeclaringClass(), value);
                return;
            }
            if (Integer.class.equals(field.getType())) {
                field.set(field.getDeclaringClass(), Integer.parseInt(value));
                return;
            }
            if (Long.class.equals(field.getType())) {
                field.set(field.getDeclaringClass(), Long.parseLong(value));
                return;
            }
            if (Float.class.equals(field.getType())) {
                field.set(field.getDeclaringClass(), Float.parseFloat(value));
                return;
            }
            if (Double.class.equals(field.getType())) {
                field.set(field.getDeclaringClass(), Double.parseDouble(value));
                return;
            }
            field.set(field.getDeclaringClass(), objectMapper.readValue(value, field.getType()));
        } catch (Exception e) {
            log.error("field name={} type={} value={} 设置值异常", field.getName(), field.getType(), value, e);
        }
    }

    /**
     * 定时全量从server端拉取数据更新缓存
     */
    private void startScheduledTask() {
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            try {
                List<ConfigDTO> configDTOS = pullFromServer();
                Map<String, String> configsFromServer = configDTOS.stream()
                        .collect(Collectors.toMap(ConfigDTO::getKey, ConfigDTO::getValue));
                updateAndPersistCache(configsFromServer, true);
            } catch (Throwable th) {
                log.error("定时全量从server端拉取数据更新缓存异常", th);
            }
        }, 5, period, TimeUnit.SECONDS);
    }

    /**
     * 更新并持久化缓存
     * @param configs
     * @param fullyUpdate 是否全量更新
     */
    private void updateAndPersistCache(Map<String, String> configs, boolean fullyUpdate) {
        if (configs == null || configs.size() == 0) {
            return;
        }

        boolean isSpaceChanged = false;
        List<String> changedKeys = new ArrayList<>();

        for (Map.Entry<String, String> entry : configs.entrySet()) {
            if (!Objects.equals(entry.getValue(), configCache.get(entry.getKey()))) {
                Set<Field> fields = keyToFieldsMap.get(entry.getKey());
                if (CollectionUtils.isEmpty(fields)) {
                    continue;
                }

                // 更新注解字段
                fields.forEach(field -> setFieldValue(field, entry.getValue()));

                // 通知 item listener
                if (keyToConfigItemChangeListenersMap.containsKey(entry.getKey())) {
                    ItemChangeContext context = ItemChangeContext.builder()
                            .application(application)
                            .env(env)
                            .key(entry.getKey())
                            .oldValue(configCache.get(entry.getKey()))
                            .newValue(entry.getValue()).build();
                    List<ConfigItemChangeListener> listeners = keyToConfigItemChangeListenersMap.get(entry.getKey());
                    listeners.forEach(listener -> {
                        listener.process(context);
                    });
                }

                // 部分更新cache
                if (!fullyUpdate) {
                    configCache.put(entry.getKey(), entry.getValue());
                }


                changedKeys.add(entry.getKey());
                isSpaceChanged = true;
            }
        }

        // 全量更新cache
        if (fullyUpdate) {
            configCache = configs;
        }

        // 通知 space listener
        if (isSpaceChanged) {
            configSpaceChangeListeners.forEach(listener -> {
                SpaceChangeContext context = SpaceChangeContext.builder()
                        .application(application)
                        .env(env)
                        .changedKeys(changedKeys)
                        .build();
                listener.process(context);
            });
        }

        try {
            // 缓存持久化
            Files.write(objectMapper.writeValueAsBytes(configCache), cacheFile);
        } catch (IOException e) {
            log.error("dcc缓存持久化异常", e);
        }
    }

    public void initZkClientAndRegisterListener() throws Exception {
        this.zkClient = CuratorFrameworkFactory.builder()
                .connectString(ZK_URL)
                .sessionTimeoutMs(SESSION_TIMEOUT_MS)
                .connectionTimeoutMs(CONNECTION_TIMEOUT_MS)
                .retryPolicy(new ExponentialBackoffRetry(1000, 100))
                .build();
        this.zkClient.start();

        PathChildrenCache pathChildrenCache = new PathChildrenCache(this.zkClient, ZkUtil.path(application, env), true);
        pathChildrenCache.getListenable().addListener(new ApplicationConfigsListener());
        pathChildrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
    }

    class ApplicationConfigsListener implements PathChildrenCacheListener {
        @Override
        public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
            if (pathChildrenCacheEvent.getType() != PathChildrenCacheEvent.Type.CHILD_ADDED
                    && pathChildrenCacheEvent.getType() != PathChildrenCacheEvent.Type.CHILD_UPDATED
                    && pathChildrenCacheEvent.getType() != PathChildrenCacheEvent.Type.CHILD_REMOVED) {
                log.info("Received zookeeper event, type={}", pathChildrenCacheEvent.getType());
                return;
            }

            String configJson = new String(pathChildrenCacheEvent.getData().getData());
            log.info("Received zookeeper event, type={}, path={}, data={}",
                    pathChildrenCacheEvent.getType(), pathChildrenCacheEvent.getData().getPath(), configJson);
            ConfigDTO configDTO = objectMapper.readValue(configJson, ConfigDTO.class);
            if (configDTO == null) {
                log.error("Parsed configDTO from zookeeper node is null");
                return;
            }

            if (pathChildrenCacheEvent.getType() == PathChildrenCacheEvent.Type.CHILD_ADDED) {
                Map<String, String> configs = new HashMap<>();
                configs.put(configDTO.getKey(), configDTO.getValue());
                updateAndPersistCache(configs, false);
            }
            if (pathChildrenCacheEvent.getType() == PathChildrenCacheEvent.Type.CHILD_UPDATED) {
                Map<String, String> configs = new HashMap<>();
                configs.put(configDTO.getKey(), configDTO.getValue());
                updateAndPersistCache(configs, false);
            }
            // TODO 删除配置处理
            if (pathChildrenCacheEvent.getType() == PathChildrenCacheEvent.Type.CHILD_REMOVED) {
                configCache.remove(configDTO.getKey());
            }
        }
    }

    /**
     * 客户端注册监听器
     */
    public void registerListener(String key, ConfigItemChangeListener listener) {
        if (StringUtils.isEmpty(key)) {
            throw new IllegalArgumentException("key is empty");
        }
        List<ConfigItemChangeListener> listeners = keyToConfigItemChangeListenersMap.getOrDefault(key, new ArrayList<>());
        listeners.add(listener);
        keyToConfigItemChangeListenersMap.put(key, listeners);
    }
    public void registerListener(ConfigSpaceChangeListener listener) {
        configSpaceChangeListeners.add(listener);
    }

    /**
     * spring bean destroy
     */
    public void destroy() {
        this.zkClient.close();
        this.scheduledExecutorService.shutdownNow();
        this.configCache.clear();
        this.keyToFieldsMap.clear();
        this.configSpaceChangeListeners.clear();
        this.keyToConfigItemChangeListenersMap.clear();
    }

}
