package lewiszlw.dcc.client;

import com.google.common.base.Preconditions;
import lewiszlw.dcc.client.annotation.DccConfig;
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

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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
    private String group = "default";
    private String scanBasePackages = ".";
    private Reflections reflections;

    private Map<String, String> configCache = new ConcurrentHashMap<>();

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
        ConfigDTO configDTO = configDubboService.queryConfig(application, env, group, key);
        return Objects.isNull(configDTO)? null: configDTO.getValue();
    }

    public List<ConfigDTO> allConfigs() {
        return configDubboService.queryAllConfigs(application, env, group);
    }

    /**
     * spring bean init
     */
    public void init() {
        // 初始化参数并检查
        initParamAndCheck();
        // 全量拉取配置到cache
        initCache();
        // 扫描注解，给字段注入配置value并监听
        scanAnnotation();
    }

    /**
     * 初始化参数并检查
     */
    private void initParamAndCheck() {
        Preconditions.checkArgument(!StringUtils.isEmpty(application), "application is empty");
        Preconditions.checkArgument(env != null, "env is null");
        reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(scanBasePackages))
                .setScanners(new FieldAnnotationsScanner()));
    }

    /**
     * 全量拉取配置并更新缓存
     */
    private void initCache() {
        configCache.putAll(pull().stream().collect(Collectors.toMap(ConfigDTO::getKey, ConfigDTO::getValue)));
    }

    /**
     * 全量拉数据
     */
    private List<ConfigDTO> pull() {
        // TODO 缓存持久化（文件），避免配置中心挂掉，客户端无法启动
        try {
            List<ConfigDTO> configDTOS = configDubboService
                    .queryAllConfigs(application, env, StringUtils.isEmpty(group) ? "default" : group);
            if (CollectionUtils.isEmpty(configDTOS)) {
                log.warn("DccClient全量拉取配置为空，请确认是否存在异常");
            }
            return configDTOS;
        } catch (Throwable th) {
            log.error("DccClient全量拉取配置异常，使用缓存文件加载配置，配置更新功能将无法使用", th);
            // TODO 从缓存文件加载配置
        }
        return Collections.EMPTY_LIST;
    }

    /**
     * 扫描注解并给字段设定值和监听器
     */
    private void scanAnnotation() {
        Set<Field> fields = reflections.getFieldsAnnotatedWith(DccConfig.class);
        for (Field field : fields) {
            // TODO 暂不允许拉取其他应用配置
            DccConfig dccConfig = field.getAnnotation(DccConfig.class);
            String key = StringUtils.isEmpty(dccConfig.key())? field.getName(): dccConfig.key();
            if (configCache.containsKey(key)) {
                // 监听并set值
                watchAndSetField(field, configCache.get(key));
            } else {
                log.error("key={} 未进行配置或未生效，请检查", key);
            }
        }
    }

    /**
     * 监听并设置值
     */
    private void watchAndSetField(Field field, String value) {
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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
