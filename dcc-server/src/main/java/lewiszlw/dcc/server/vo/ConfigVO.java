package lewiszlw.dcc.server.vo;

import lewiszlw.dcc.iface.constant.Env;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-04-29
 */
@Data
@Accessors(chain = true)
public class ConfigVO {

    private String application;

    private Env env;

    private String group;

    private String key;

    private String value;

    private String comment;
}
