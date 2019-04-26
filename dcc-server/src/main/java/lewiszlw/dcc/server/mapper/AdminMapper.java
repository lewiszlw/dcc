package lewiszlw.dcc.server.mapper;

import lewiszlw.dcc.server.entity.AdminEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-04-24
 */
@Repository
@Mapper
public interface AdminMapper {

    /**
     * 查询所有管理员
     * @return
     */
    List<AdminEntity> selectAll();

    /**
     * 批量插入管理员
     */
    int batchInsert(List<AdminEntity> adminEntities);

    /**
     * 批量更新管理员
     */
    int batchUpdate(List<AdminEntity> adminEntities);

    /**
     * 批量删除管理员
     */
    int batchDelete(List<String> adminIds);

}
