package lewiszlw.dcc.server.mapper;

import lewiszlw.dcc.server.entity.AdminEntity;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-04-24
 */
public interface AdminMapper {

    int insert(AdminEntity adminEntity);

    int update(AdminEntity adminEntity);

    int delete(String adminId);
}
