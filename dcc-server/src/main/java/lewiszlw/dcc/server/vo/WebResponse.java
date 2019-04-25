package lewiszlw.dcc.server.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-04-25
 */
@Data
@Accessors(chain = true)
public class WebResponse {

    private static final String SUCCESS = "success";
    private static final String FAILURE = "failure";

    /**
     * 状态
     */
    private String status;

    /**
     * 信息
     */
    private String msg;

    /**
     * 数据
     */
    private Object data;

    public static WebResponse createSuccessWebResponse(Object data) {
        return new WebResponse().setStatus(SUCCESS).setMsg("请求成功").setData(data);
    }

    public static WebResponse createFailWebResponse(Object data) {
        return new WebResponse().setStatus(FAILURE).setMsg("请求失败").setData(data);
    }

    public static WebResponse createFailWebResponse(String msg, Object data) {
        return new WebResponse().setStatus(FAILURE).setMsg(msg).setData(data);
    }
}
