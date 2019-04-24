package lewiszlw.dcc.server.entity;

import lewiszlw.dcc.server.constant.AdminType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * Desc: 管理员实体
 *
 * @author zhanglinwei02
 * @date 2019-04-24
 */
@Data
@Accessors(chain = true)
public class AdminEntity {

    /**
     * 主键id
     */
    private int id;

    /**
     * 应用标识
     */
    private String application;

    /**
     * 管理员类型
     */
    private AdminType adminType;

    /**
     * 管理员标识
     */
    private String adminId;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新时间
     */
    private Date updatedTime;
}
