package lewiszlw.dcc.server.entity;

import lewiszlw.dcc.server.constant.RecordType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * Desc: 记录实体
 *
 * @author zhanglinwei02
 * @date 2019-04-24
 */
@Data
@Accessors(chain = true)
public class RecordEntity {

    /**
     * 主键
     */
    private int id;

    /**
     * 记录类型
     */
    private RecordType recordType;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 应用
     */
    private String application;

    /**
     * 创建时间
     */
    private Date createdTime;

}
