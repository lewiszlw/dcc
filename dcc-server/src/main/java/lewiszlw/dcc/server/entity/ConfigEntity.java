package lewiszlw.dcc.server.entity;

import lewiszlw.dcc.iface.constant.Env;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * Desc: 配置实体
 *
 * @author zhanglinwei02
 * @date 2019-04-24
 */
@Data
@Accessors(chain = true)
public class ConfigEntity {

    /**
     * 主键
     */
    private int id;

    /**
     * 应用
     */
    private String application;

    /**
     * 环境
     */
    private Env env;

    /**
     * 分组
     */
    private String group;

    /**
     * 配置项名称
     */
    private String key;

    /**
     * 配置项版本
     */
    private int version;

    /**
     * 配置项值
     */
    private String value;

    /**
     * 配置项注释
     */
    private String comment;

    /**
     * 创建时间
     */
    private Date createdTime;
}
