package lewiszlw.dcc.server.service;

import lewiszlw.dcc.server.entity.AdminEntity;

import java.util.List;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-04-26
 */
public interface AdminService {

    /**
     * 查询所有管理员
     */
    List<AdminEntity> selectAll();
}
