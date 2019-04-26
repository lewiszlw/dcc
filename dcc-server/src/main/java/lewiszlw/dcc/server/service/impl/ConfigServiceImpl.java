package lewiszlw.dcc.server.service.impl;

import lewiszlw.dcc.server.entity.ConfigEntity;
import lewiszlw.dcc.server.mapper.ConfigMapper;
import lewiszlw.dcc.server.service.ConfigService;
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
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private ConfigMapper configMapper;

    @Override
    public List<ConfigEntity> allConfigs() {
        return configMapper.selectAll();
    }
}
