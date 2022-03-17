package lewiszlw.dcc.iface;

import lewiszlw.dcc.iface.constant.Env;
import lewiszlw.dcc.iface.response.ConfigDTO;

import java.util.List;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-04-26
 */
public interface ConfigDubboService {

    /**
     * 获取应用所有配置
     */
    List<ConfigDTO> queryConfigs(String application, Env env);

    /**
     * 获取应用某个配置
     */
    ConfigDTO queryConfig(String application, Env env, String key);
}
