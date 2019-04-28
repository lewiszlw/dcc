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
    List<ConfigDTO> queryAllConfigs(String application, Env env, String group);

    /**
     * 获取应用某个配置
     */
    ConfigDTO queryConfig(String application, Env env, String group, String key);
}
