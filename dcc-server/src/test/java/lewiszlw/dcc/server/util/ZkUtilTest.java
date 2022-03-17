package lewiszlw.dcc.server.util;

import lewiszlw.dcc.iface.constant.Env;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-05-06
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ZkUtilTest {

    @Test
    public void testPath() {
        Assert.assertTrue(ZkUtil.path("test.app", Env.TEST, "config-test")
                .equals("/dcc/test.app/TEST/default/config-test"));
        Assert.assertTrue(ZkUtil.path("test.app", Env.TEST)
                .equals("/dcc/test.app/TEST/default"));
    }
}
