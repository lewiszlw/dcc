package lewiszlw.dcc.server.vo;

import lombok.Data;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-04-25
 */
@Data
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


}
