package lewiszlw.dcc.server.service.impl;

import lewiszlw.dcc.server.entity.AdminEntity;
import lewiszlw.dcc.server.mapper.AdminMapper;
import lewiszlw.dcc.server.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-04-26
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public List<AdminEntity> selectAll() {
        return adminMapper.selectAll();
    }
}
