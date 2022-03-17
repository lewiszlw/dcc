package lewiszlw.dcc.server.service;

import lewiszlw.dcc.iface.constant.Env;
import lewiszlw.dcc.server.entity.ConfigEntity;
import lewiszlw.dcc.server.vo.AddConfigRequest;

import java.util.List;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-04-26
 */
public interface ConfigService {

    /**
     * 所有应用所有配置
     */
    List<ConfigEntity> allConfigs();

    /**
     * 获取应用所有最新配置
     */
    List<ConfigEntity> queryLatestConfigs(String application, Env env);

    /**
     * 获取应用某个配置最新版
     */
    ConfigEntity queryLatestConfig(String application, Env env, String key);

    /**
     * 批量添加配置
     */
    Integer addConfigs(AddConfigRequest addConfigRequest);
}
