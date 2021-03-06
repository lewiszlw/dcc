package lewiszlw.dcc.demo.controller;

import lewiszlw.dcc.client.DccClient;
import lewiszlw.dcc.client.annotation.DccConfig;
import lewiszlw.dcc.iface.response.ConfigDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    private static String config1;

    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }

    @RequestMapping("/dcc/all")
    public List<ConfigDTO> dccAllConfigs() {
        return dccClient.allConfigs();
    }

    @RequestMapping("/dcc/one")
    public String dccOneConfig(@RequestParam String key) {
        return dccClient.get(key);
    }

    @RequestMapping("dcc/configAnnotation")
    public String configAnnotation() {
        return config1;
    }

}
