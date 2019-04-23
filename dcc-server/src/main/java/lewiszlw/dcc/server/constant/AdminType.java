package lewiszlw.dcc.server.constant;

import lombok.Getter;

/**
 * Desc: 管理员类型
 *
 * @author zhanglinwei02
 * @date 2019-04-24
 */
@Getter
public enum AdminType {
    UNKNOWN(0, "未知"),
    SUPER(1, "超级管理员"),
    GENERAL(2, "普通管理员");

    private int type;
    private String desc;

    AdminType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
