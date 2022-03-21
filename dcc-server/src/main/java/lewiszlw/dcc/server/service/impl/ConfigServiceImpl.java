package lewiszlw.dcc.server.service.impl;

import com.google.common.collect.Lists;
import lewiszlw.dcc.iface.constant.Env;
import lewiszlw.dcc.server.constant.Constants;
import lewiszlw.dcc.server.converter.ConfigConverter;
import lewiszlw.dcc.server.entity.ConfigEntity;
import lewiszlw.dcc.server.mapper.ConfigMapper;
import lewiszlw.dcc.server.service.ConfigService;
import lewiszlw.dcc.server.util.JsonUtil;
import lewiszlw.dcc.iface.util.ZkUtil;
import lewiszlw.dcc.server.vo.AddConfigRequest;
import lewiszlw.dcc.server.vo.ConfigVO;
import lewiszlw.dcc.server.vo.RollbackRequest;
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
    public List<ConfigEntity> queryConfigVersions(String application, Env env, String key) {
        return configMapper.selectOneAllVersions(application, env, key);
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

        // 落库
        List<ConfigEntity> configEntitiesToBeInserted = new ArrayList<>();
        configEntities.forEach(configEntity -> {
            ConfigEntity oldConfigEntity = queryLatestConfig(configEntity.getApplication(),
                    configEntity.getEnv(),
                    configEntity.getKey());
            if (oldConfigEntity == null) {
                configEntity.setVersion(Constants.INIT_VERSION);
                configEntitiesToBeInserted.add(configEntity);
                return;
            }
            // 版本号+1
            if (!Objects.equals(configEntity.getValue(), oldConfigEntity.getValue())) {
                configEntity.setVersion(oldConfigEntity.getVersion() + 1);
                configEntitiesToBeInserted.add(configEntity);
                return;
            }
        });
        int result = configMapper.batchInsert(configEntitiesToBeInserted);

        // zk增加或更新节点
        configEntities.forEach(configEntity -> {
            zooKeeperService.createOrUpdate(
                    ZkUtil.configPath(configEntity.getApplication(), configEntity.getEnv(), configEntity.getKey()),
                    JsonUtil.toJson(ConfigConverter.configEntityToConfigDTO(configEntity))
            );
        });

        return result;
    }

    @Override
    public Integer rollback(RollbackRequest request) {
        ConfigEntity targetConfigEntity = configMapper.selectSpecificVersionConfig(
                request.getApplication(),
                request.getEnv(),
                request.getKey(),
                request.getTargetVersion());
        if (targetConfigEntity == null) {
            return 0;
        }
        ConfigEntity latestConfigEntity = queryLatestConfig(request.getApplication(), request.getEnv(), request.getKey());
        if (targetConfigEntity.getVersion() == latestConfigEntity.getVersion()
                || Objects.equals(targetConfigEntity.getValue(), latestConfigEntity.getValue())) {
            return 0;
        }
        targetConfigEntity.setVersion(latestConfigEntity.getVersion() + 1);
        Integer result = configMapper.batchInsert(Lists.newArrayList(targetConfigEntity));
        zooKeeperService.createOrUpdate(
                ZkUtil.configPath(targetConfigEntity.getApplication(), targetConfigEntity.getEnv(), targetConfigEntity.getKey()),
                JsonUtil.toJson(ConfigConverter.configEntityToConfigDTO(targetConfigEntity)));
        return result;
    }
}
