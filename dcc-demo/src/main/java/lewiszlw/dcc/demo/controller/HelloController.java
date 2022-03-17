package lewiszlw.dcc.demo.controller;

import lewiszlw.dcc.client.DccClient;
import lewiszlw.dcc.client.annotation.DccConfig;
import lewiszlw.dcc.iface.response.ConfigDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-04-26
 */
@RestController
public class HelloController {

    @Autowired
    private DccClient dccClient;

    @DccConfig
    private static String configKey1;

    @DccConfig(key = "configKey1")
    private static String configWithSpecificKey;

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
        return String.format("configKey1: %s, configWithSpecificKey: %s", configKey1, configWithSpecificKey);
    }

}
