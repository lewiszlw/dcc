package lewiszlw.dcc.iface;

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
     * 获取所有配置
     */
    List<ConfigDTO> allConfigs();
}
