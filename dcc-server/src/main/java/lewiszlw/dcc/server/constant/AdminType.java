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
    UNKNOWN("未知"),
    SUPER("超级管理员"),
    GENERAL("普通管理员");

    private String desc;

    AdminType(String desc) {
        this.desc = desc;
    }
}
