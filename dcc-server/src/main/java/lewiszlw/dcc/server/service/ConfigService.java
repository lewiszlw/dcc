package lewiszlw.dcc.server.service;

import lewiszlw.dcc.iface.constant.Env;
import lewiszlw.dcc.server.entity.ConfigEntity;
import lewiszlw.dcc.server.vo.AddConfigRequest;
import lewiszlw.dcc.server.vo.ConfigVO;

import java.util.List;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-04-26
 */
public interface ConfigService {

    List<ConfigEntity> allConfigs();

    /**
     * 从ZK获取应用所有最新配置
     */
    List<ConfigEntity> queryAllConfigs(String application, Env env, String group);

    /**
     * 从ZK获取应用某个配置
     */
    ConfigEntity queryConfigLatest(String application, Env env, String group, String key);

    /**
     * 批量添加配置
     */
    Integer addConfigs(AddConfigRequest addConfigRequest);

    /**
     * 从DB查询application，env，group下所有版本配置
     */
    List<ConfigEntity> queryConfigsAllVersion(String application, Env env, String group);

    /**
     * 从DB查询application，env下所有版本配置
     */
    List<ConfigEntity> queryConfigsAllVersion(String application, Env env);
}
