package lewiszlw.dcc.demo.controller;

import lewiszlw.dcc.client.DccClient;
import lewiszlw.dcc.client.annotation.DccConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-04-26
 */
@RestController
@Slf4j
public class HelloController {

    @Autowired
    private DccClient dccClient;

    @DccConfig
    private static String configKey1;

    @DccConfig(key = "configKey1")
    private static String configWithSpecificKey;

    @DccConfig
    private static int configIntKey;

    @DccConfig(key = "configLongKey")
    private static Long configLongKey;

    @DccConfig
    private static float configFloatKey;

    @DccConfig
    private static Double configDoubleKey;

    @DccConfig
    private static Student configObjKey;

    @DccConfig
    private static Map<String, Student> configMapKey;

    @DccConfig
    private static List<Student> configListKey;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Student {
        private String name;
        private Integer age;
        private Boolean isMale;
    }

    @PostConstruct
    private void init() {
        dccClient.registerListener(context -> log.info("ConfigSpaceChangeListener received event, context: {}", context));
        dccClient.registerListener("configKey1", context -> log.info("ConfigItemChangeListener received event, context: {}", context));
    }

    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }

    @RequestMapping("/dcc/all")
    public Map<String, String> dccAllConfigs() {
        return dccClient.getAll();
    }

    @RequestMapping("/dcc/one")
    public String dccOneConfig(@RequestParam String key) {
        return dccClient.get(key);
    }

    @RequestMapping("dcc/configAnnotation")
    public String configAnnotation() {
        return String.format("configKey1: %s, configWithSpecificKey: %s, configIntKey: %d, configLongKey: %s, configFloatKey: %f, configDoubleKey: %f, configObjKey: %s, configMapKey: %s, configListKey: %s",
                configKey1, configWithSpecificKey, configIntKey, configLongKey, configFloatKey, configDoubleKey, configObjKey, configMapKey, configListKey);
    }

}
