package lewiszlw.dcc.server.mapper;

import lewiszlw.dcc.iface.constant.Env;
import lewiszlw.dcc.server.entity.ConfigEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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

    /**
     * 查询应用所有配置的所有版本
     */
    List<ConfigEntity> batchSelectAllVersions(@Param("application") String application,
                                               @Param("env") Env env,
                                               @Param("group") String group);

    /**
     * 查询一个配置所有版本
     */
    List<ConfigEntity> selectOneAllVersions(@Param("application") String application,
                                    @Param("env") Env env,
                                    @Param("group") String group,
                                    @Param("key") String key);

    /**
     * 批量插入
     */
    Integer batchInsert(@Param("configEntities") List<ConfigEntity> configEntities);

    /**
     * 批量删除，仅用于测试
     */
    Integer batchDelete(@Param("ids") List<Integer> ids);
}
