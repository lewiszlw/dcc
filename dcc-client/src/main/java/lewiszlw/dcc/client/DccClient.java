package lewiszlw.dcc.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.io.Files;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lewiszlw.dcc.client.annotation.DccConfig;
import lewiszlw.dcc.client.util.FileUtil;
import lewiszlw.dcc.iface.ConfigDubboService;
import lewiszlw.dcc.iface.constant.Env;
import lewiszlw.dcc.iface.response.ConfigDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
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
public class DccClient {

    private String application;
    private Env env;
    private String scanBasePackages = ".";
    private String cacheFilePath = "/opt/dcc/cache";
    private long period = 180;

    private Reflections reflections;
    private File cacheFile;
    private ObjectMapper objectMapper;
    private ScheduledExecutorService scheduledExecutorService;
    private volatile Map<String, String> configCache;
    private Map<String, Set<Field>> keyToFieldsMap;

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
    @PostConstruct
    private void init() throws IOException {
        // 初始化参数并检查
        initParamAndCheck();
        // 全量拉取配置到cache
        updateAndPersistCache(initLoadConfigs(), true);
        // 扫描注解，给字段注入配置value并监听
        scanAnnotation();
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
            log.debug("从server端全量拉取配置数据：{}", objectMapper.writeValueAsString(configDTOS));
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
            String key = StringUtils.isEmpty(dccConfig.key())? field.getName(): dccConfig.key();
            if (configCache.containsKey(key)) {
                // 保存 key与fields 映射关系
                Set<Field> fieldsUsingSameKey = keyToFieldsMap.getOrDefault(key, new HashSet<>());
                fieldsUsingSameKey.add(field);
                keyToFieldsMap.put(key, fieldsUsingSameKey);
                // 设置field值
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
            // TODO 暂时只支持String
            // TODO 给字段设置监听器
            field.setAccessible(true);
            field.set(field.getDeclaringClass(), value);
        } catch (IllegalAccessException e) {
            log.error("field={} value={} 设置值异常", field.getName(), value, e);
        }
    }

    /**
     * 定时全量从server端拉取数据更新缓存
     */
    private void startScheduledTask() {
        // TODO 测试先5s后执行 后续改为600秒
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            List<ConfigDTO> configDTOS = pullFromServer();
            Map<String, String> configsFromServer = configDTOS.stream()
                    .collect(Collectors.toMap(ConfigDTO::getKey, ConfigDTO::getValue));
            updateAndPersistCache(configsFromServer, true);
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
        if (fullyUpdate) {
            // 全量更新
            for (Map.Entry<String, String> entry : configs.entrySet()) {
                // TODO 如果字段发生改变，通知listener
                if (!Objects.equals(entry.getValue(), configCache.get(entry.getKey()))) {
                    Set<Field> fields = keyToFieldsMap.get(entry.getKey());
                    if (CollectionUtils.isEmpty(fields)) {
                        continue;
                    }
                    fields.forEach(field -> setFieldValue(field, entry.getValue()));
                }
            }
            configCache = configs;
        } else {
            // TODO 增量更新
        }
        try {
            // 缓存持久化
            Files.write(objectMapper.writeValueAsBytes(configCache), cacheFile);
        } catch (IOException e) {
            log.error("dcc缓存持久化异常", e);
        }
    }

    /**
     * spring bean destroy
     */
    public void destroy() {
    }

    /**
     * ===================Getter & Setter=====================
     */
    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public Env getEnv() {
        return env;
    }

    public void setEnv(Env env) {
        this.env = env;
    }

    public String getScanBasePackages() {
        return scanBasePackages;
    }

    public void setScanBasePackages(String scanBasePackages) {
        this.scanBasePackages = scanBasePackages;
    }

    public String getCacheFilePath() {
        return cacheFilePath;
    }

    public void setCacheFilePath(String cacheFilePath) {
        this.cacheFilePath = cacheFilePath;
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }
}
