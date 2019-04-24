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
    UNKNOWN("未知"),

    /**配置操作*/
    ADD_CONFIG("新增配置"),
    CHANGE_CONFIG("修改配置"),
    DELETE_CONFIG("删除配置"),

    /**分组操作*/
    ADD_GROUP("新增分组"),
    CHANGE_GROUP("修改分组"),
    DELETE_GROUP("删除分组");

    private String desc;

    RecordType(String desc) {
        this.desc = desc;
    }

}
