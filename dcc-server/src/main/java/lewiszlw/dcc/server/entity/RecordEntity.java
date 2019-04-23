package lewiszlw.dcc.server.entity;

import lewiszlw.dcc.server.constant.RecordType;
import lombok.Data;
import lombok.experimental.Accessors;

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
     * @see RecordType
     */
    private int recordType;

    /**
     * 应用配置空间
     */
    private String namespace;



}
