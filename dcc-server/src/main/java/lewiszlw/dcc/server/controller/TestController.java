package lewiszlw.dcc.server.controller;

import lewiszlw.dcc.server.mapper.AdminMapper;
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
public class TestController {

    @Autowired
    private AdminMapper adminMapper;

    @RequestMapping("/home")
    public String home() {
        return "Welcome to home!";
    }
}
