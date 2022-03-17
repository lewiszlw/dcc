package lewiszlw.dcc.server.controller;

import lewiszlw.dcc.iface.constant.Env;
import lewiszlw.dcc.server.service.ConfigService;
import lewiszlw.dcc.server.vo.AddConfigRequest;
import lewiszlw.dcc.server.vo.WebResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-04-25
 */
@RestController
@RequestMapping("/dcc/config")
@Slf4j
public class ConfigController {

    @Autowired
    private ConfigService configService;

    @RequestMapping("/internal-all")
    public WebResponse internalAll() {
        return WebResponse.createSuccessWebResponse(configService.allConfigs());
    }

    @RequestMapping("/all")
    public WebResponse all(@RequestParam String application, @RequestParam Env env) {
        log.info("Query all latest configs for application: {} and env: {}", application, env);
        return WebResponse.createSuccessWebResponse(configService.queryLatestConfigs(application, env));
    }

    @RequestMapping("/latest")
    public WebResponse queryConfigLatest(@RequestParam String application,
                                         @RequestParam Env env,
                                         @RequestParam String key) {
        return WebResponse.createSuccessWebResponse(configService.queryLatestConfig(application, env, key));
    }

    @PostMapping("/add")
    public WebResponse addConfigs(@RequestBody AddConfigRequest addConfigRequest) {
        return WebResponse.createSuccessWebResponse(configService.addConfigs(addConfigRequest));
    }

}
