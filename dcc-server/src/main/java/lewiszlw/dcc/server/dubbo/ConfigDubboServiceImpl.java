package lewiszlw.dcc.server.dubbo;

import lewiszlw.dcc.iface.ConfigDubboService;
import lewiszlw.dcc.iface.constant.Env;
import lewiszlw.dcc.iface.response.ConfigDTO;
import lewiszlw.dcc.server.converter.ConfigConverter;
import lewiszlw.dcc.server.entity.ConfigEntity;
import lewiszlw.dcc.server.service.ConfigService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-04-26
 */
@Service(version = "1.0.0")
public class ConfigDubboServiceImpl implements ConfigDubboService {

    @Autowired
    private ConfigService configService;

    @Override
    public List<ConfigDTO> queryAllConfigs(String application, Env env, String group) {
        List<ConfigEntity> configEntities = configService.queryConfigsLatest(application, env, group);
        if (CollectionUtils.isEmpty(configEntities)) {
            return Collections.EMPTY_LIST;
        }
        return configEntities.stream().map(ConfigConverter::configEntityToConfigDTO).collect(Collectors.toList());
    }

    @Override
    public ConfigDTO queryConfig(String application, Env env, String group, String key) {
        ConfigEntity configEntity = configService.queryConfigLatest(application, env, group, key);
        return Objects.isNull(configEntity)? null: ConfigConverter.configEntityToConfigDTO(configEntity);
    }
}
