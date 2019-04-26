package lewiszlw.dcc.server.controller;

import lewiszlw.dcc.server.service.ConfigService;
import lewiszlw.dcc.server.vo.WebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @RequestMapping("/all")
    public WebResponse all() {
        return WebResponse.createSuccessWebResponse(configService.allConfigs());
    }

}
