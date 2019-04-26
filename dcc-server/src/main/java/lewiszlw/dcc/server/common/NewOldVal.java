package lewiszlw.dcc.server.common;

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
public class NewOldVal {

    /**
     * 字段
     */
    private String field;

    /**
     * 旧值
     */
    private Object oldVal;

    /**
     * 新值
     */
    private Object newVal;
}
