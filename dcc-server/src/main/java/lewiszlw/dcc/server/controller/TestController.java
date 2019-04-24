package lewiszlw.dcc.server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-04-25
 */
@RestController
public class TestController {

    @RequestMapping("/home")
    public String home() {
        return "Welcome to home!";
    }
}
