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

    int insert(AdminEntity adminEntity);

    int update(AdminEntity adminEntity);

    int delete(String adminId);

    List<AdminEntity> selectAll();
}
