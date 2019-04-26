package lewiszlw.dcc.iface.response;

import lewiszlw.dcc.iface.constant.Env;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-04-26
 */
@Data
@Accessors(chain = true)
public class ConfigDTO implements Serializable {

    private String application;

    private Env env;

    private String group;

    private String key;

    private Integer version;

    private String value;

    private String comment;
}
