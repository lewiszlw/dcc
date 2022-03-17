package lewiszlw.dcc.server.service.impl;

import lewiszlw.dcc.iface.constant.Env;
import lewiszlw.dcc.server.constant.Constants;
import lewiszlw.dcc.server.converter.ConfigConverter;
import lewiszlw.dcc.server.entity.ConfigEntity;
import lewiszlw.dcc.server.mapper.ConfigMapper;
import lewiszlw.dcc.server.service.ConfigService;
import lewiszlw.dcc.server.vo.AddConfigRequest;
import lewiszlw.dcc.server.vo.ConfigVO;
import lewiszlw.dcc.server.zookeeper.ZooKeeperService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private ConfigMapper configMapper;

    @Autowired
    private ZooKeeperService zooKeeperService;

    @Override
    public List<ConfigEntity> allConfigs() {
        return configMapper.selectAll();
    }

    @Override
    public List<ConfigEntity> queryLatestConfigs(String application, Env env) {
        // 获取该应用所有版本配置
        List<ConfigEntity> configEntities = configMapper.batchSelectAllVersions(application, env);
        if (CollectionUtils.isEmpty(configEntities)) {
            return Collections.emptyList();
        }

        // 过滤得到所有最新版本配置
        Map<String, ConfigEntity> keyToLatestConfigEntityMap = new HashMap<>();
        for (ConfigEntity configEntity : configEntities) {
            if (keyToLatestConfigEntityMap.containsKey(configEntity.getKey())) {
                if (configEntity.getVersion() > keyToLatestConfigEntityMap.get(configEntity.getKey()).getVersion()) {
                    keyToLatestConfigEntityMap.put(configEntity.getKey(), configEntity);
                }
            } else {
                keyToLatestConfigEntityMap.put(configEntity.getKey(), configEntity);
            }
        }

        return new ArrayList<>(keyToLatestConfigEntityMap.values());
    }

    @Override
    public ConfigEntity queryLatestConfig(String application, Env env, String key) {
        // 获取该应用所有版本配置
        List<ConfigEntity> configEntities = configMapper.selectOneAllVersions(application, env, key);
        if (CollectionUtils.isEmpty(configEntities)) {
            return null;
        }

        // 根据版本升序排列
        List<ConfigEntity> sortedConfigEntities = configEntities.stream()
                .sorted(Comparator.comparing(ConfigEntity::getVersion)).collect(Collectors.toList());

        return sortedConfigEntities.get(sortedConfigEntities.size() - 1);
    }

    @Override
    public Integer addConfigs(AddConfigRequest addConfigRequest) {
        List<ConfigVO> configVOs = addConfigRequest.getConfigVOs();
        if (CollectionUtils.isEmpty(configVOs)) {
            return 0;
        }
        List<ConfigEntity> configEntities = configVOs.stream()
                .map(ConfigConverter::configVOToConfigEntity)
                .collect(Collectors.toList());
        // TODO zk增加或更新节点
//        configEntities.stream().forEach(configEntity -> {
//            zooKeeperService.create(
//                    ZkUtil.path(configEntity.getApplication(),
//                            configEntity.getEnv(),
//                            configEntity.getKey()),
//                    JsonUtil.toJson(configEntity)
//            );
//        });
        // 落库
        // 版本号+1
        configEntities.stream().forEach(configEntity -> {
            ConfigEntity oldConfigEntity = queryLatestConfig(configEntity.getApplication(),
                    configEntity.getEnv(),
                    configEntity.getKey());
            configEntity.setVersion(oldConfigEntity == null ? Constants.INIT_VERSION : oldConfigEntity.getVersion() + 1);
        });
        return configMapper.batchInsert(configEntities);
    }
}
