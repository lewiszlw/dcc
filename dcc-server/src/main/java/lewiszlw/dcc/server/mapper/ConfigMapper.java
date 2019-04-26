package lewiszlw.dcc.server.mapper;

import lewiszlw.dcc.server.entity.ConfigEntity;
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
public interface ConfigMapper {

    List<ConfigEntity> selectAll();
}
