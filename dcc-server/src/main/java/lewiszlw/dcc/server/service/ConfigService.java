package lewiszlw.dcc.server.service;

import lewiszlw.dcc.iface.constant.Env;
import lewiszlw.dcc.server.entity.ConfigEntity;

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
     * 获取应用所有最新配置
     */
    List<ConfigEntity> queryAllConfigs(String application, Env env, String group);

    /**
     * 获取应用某个配置
     */
    ConfigEntity queryConfigLatest(String application, Env env, String group, String key);
}
