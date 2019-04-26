package lewiszlw.dcc.client;

import lewiszlw.dcc.iface.ConfigDubboService;
import lewiszlw.dcc.iface.constant.Env;
import lewiszlw.dcc.iface.response.ConfigDTO;
import org.apache.dubbo.config.annotation.Reference;

import java.util.List;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-04-26
 */
public class DccClient {

    private String application;
    private Env env;

    @Reference(version = "1.0.0", check = false)
    private ConfigDubboService configDubboService;

    public List<ConfigDTO> allConfigs() {
        return configDubboService.allConfigs();
    }

    /**
     * ===================Getter & Setter=====================
     */
    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public Env getEnv() {
        return env;
    }

    public void setEnv(Env env) {
        this.env = env;
    }
}
