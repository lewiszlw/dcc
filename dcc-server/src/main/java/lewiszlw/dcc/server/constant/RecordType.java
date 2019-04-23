package lewiszlw.dcc.server.constant;

import lombok.Getter;

/**
 * Desc: 记录类型
 *
 * @author zhanglinwei02
 * @date 2019-04-24
 */
@Getter
public enum RecordType {
    UNKNOWN(0, "未知");

    private int type;
    private String desc;

    RecordType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

}
