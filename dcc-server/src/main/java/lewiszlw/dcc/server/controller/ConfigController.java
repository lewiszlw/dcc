package lewiszlw.dcc.server.controller;

import lewiszlw.dcc.iface.constant.Env;
import lewiszlw.dcc.server.service.ConfigService;
import lewiszlw.dcc.server.vo.WebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-04-25
 */
@RestController
@RequestMapping("/dcc/config")
public class ConfigController {

    @Autowired
    private ConfigService configService;

    @RequestMapping("/allConfigs")
    public WebResponse allConfigs() {
        return WebResponse.createSuccessWebResponse(configService.allConfigs());
    }

    @RequestMapping("/all")
    public WebResponse all(@RequestParam String application, @RequestParam Env env, @RequestParam String group) {
        return WebResponse.createSuccessWebResponse(configService.queryConfigsLatest(application, env, group));
    }

    @RequestMapping("/latest")
    public WebResponse queryConfigLatest(@RequestParam String application,
                                         @RequestParam Env env,
                                         @RequestParam String group,
                                         @RequestParam String key) {
        return WebResponse.createSuccessWebResponse(configService.queryConfigLatest(application, env, group, key));
    }

}
