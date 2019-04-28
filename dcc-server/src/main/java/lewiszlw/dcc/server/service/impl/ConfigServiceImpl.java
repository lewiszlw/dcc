package lewiszlw.dcc.server.service.impl;

import lewiszlw.dcc.iface.constant.Env;
import lewiszlw.dcc.server.entity.ConfigEntity;
import lewiszlw.dcc.server.mapper.ConfigMapper;
import lewiszlw.dcc.server.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

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

    @Override
    public List<ConfigEntity> queryAllConfigs(String application, Env env, String group) {
        // TODO optimize
        List<ConfigEntity> allVersionConfigs = configMapper.batchSelectAllVersions(application, env, group);
        Map<String, ConfigEntity> allConfigsMap = new HashMap<>();
        for (ConfigEntity configEntity : allVersionConfigs) {
            ConfigEntity tmp = allConfigsMap.get(configEntity.getKey());
            if (tmp != null && configEntity.getVersion() > tmp.getVersion()
                    || tmp == null) {
                allConfigsMap.put(configEntity.getKey(), configEntity);
            }
        }
        if (CollectionUtils.isEmpty(allConfigsMap)) {
            return Collections.EMPTY_LIST;
        }
        return allConfigsMap.values().stream().collect(Collectors.toList());
    }

    @Override
    public ConfigEntity queryConfigLatest(String application, Env env, String group, String key) {
        List<ConfigEntity> configEntities = configMapper.selectOneAllVersions(application, env, group, key);
        return configEntities.stream().sorted(new Comparator<ConfigEntity>() {
            @Override
            public int compare(ConfigEntity o1, ConfigEntity o2) {
                return o2.getVersion() - o1.getVersion();
            }
        }).findFirst().orElse(null);
    }
}
