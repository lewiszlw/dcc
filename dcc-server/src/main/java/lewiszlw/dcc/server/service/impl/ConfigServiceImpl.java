package lewiszlw.dcc.server.service.impl;

import com.google.common.collect.Lists;
import lewiszlw.dcc.iface.constant.Env;
import lewiszlw.dcc.server.converter.ConfigConverter;
import lewiszlw.dcc.server.entity.ConfigEntity;
import lewiszlw.dcc.server.mapper.ConfigMapper;
import lewiszlw.dcc.server.service.ConfigService;
import lewiszlw.dcc.server.util.JsonUtil;
import lewiszlw.dcc.server.util.ZkUtil;
import lewiszlw.dcc.server.vo.AddConfigRequest;
import lewiszlw.dcc.server.vo.ConfigVO;
import lewiszlw.dcc.server.zookeeper.ZooKeeperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    @Autowired
    private ZooKeeperService zooKeeperService;

    @Override
    public List<ConfigEntity> allConfigs() {
        return configMapper.selectAll();
    }

    @Override
    public List<ConfigEntity> queryConfigsLatest(String application, Env env, String group) {
        List<String> children = zooKeeperService.getChildren(ZkUtil.path(application, env, group));
        if (!CollectionUtils.isEmpty(children)) {
            List<ConfigEntity> list = new ArrayList<>();
            for (String key : children) {
                String data = zooKeeperService.get(ZkUtil.path(application, env, group, key));
                list.add(JsonUtil.fromJson(data, ConfigEntity.class));
            }
            return list;
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public ConfigEntity queryConfigLatest(String application, Env env, String group, String key) {
        String data = zooKeeperService.get(ZkUtil.path(application, env, group, key));
        return JsonUtil.fromJson(data, ConfigEntity.class);
    }

    @Override
    public Integer addConfigs(AddConfigRequest addConfigRequest) {
        List<ConfigVO> configVOS = addConfigRequest.getConfigVOS();
        if (CollectionUtils.isEmpty(configVOS)) {
            return 0;
        }
        List<ConfigEntity> configEntities = configVOS.stream()
                .map(ConfigConverter::configVOToConfigEntity)
                .map(configEntity -> {return configEntity.setVersion(1);})
                .collect(Collectors.toList());
        // zk新增节点
        configEntities.stream().forEach(configEntity -> {
            zooKeeperService.create(
                    ZkUtil.path(configEntity.getApplication(),
                            configEntity.getEnv(),
                            configEntity.getGroup(),
                            configEntity.getKey()),
                    JsonUtil.toJson(configEntity)
            );
        });
        // 落库
        writeDB(configEntities);
        return configEntities.size();
    }

    private synchronized Integer writeDB(List<ConfigEntity> configEntities) {
        // TODO
        return 0;
    }


    @Override
    public List<ConfigEntity> queryConfigsAllVersion(String application, Env env, String group, String key) {
        return configMapper.selectOneAllVersions(application, env, group, key);
    }

}
