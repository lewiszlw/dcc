package lewiszlw.dcc.server.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import lewiszlw.dcc.server.vo.WebResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-05-06
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class JsonUtilTest {

    @Test
    public void testToJson() throws JsonProcessingException {
        WebResponse webResponse = WebResponse.createSuccessWebResponse("testJson");
        String json = JsonUtil.toJson(webResponse);
        Assert.assertTrue(json.equals("{\"status\":\"success\",\"msg\":\"请求成功\",\"data\":\"testJson\"}"));
    }

    @Test
    public void testFromJson() throws IOException {
        String json = "{\"status\":\"success\",\"msg\":\"请求成功\",\"data\":\"testJson\"}";
        WebResponse webResponse = JsonUtil.fromJson(json, WebResponse.class);
        Assert.assertTrue(webResponse.getStatus().equals("success"));
        Assert.assertTrue(webResponse.getMsg().equals("请求成功"));
        Assert.assertTrue(webResponse.getData().equals("testJson"));
    }
}
