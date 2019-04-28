package lewiszlw.dcc.demo.config;

import lewiszlw.dcc.client.DccClient;
import lewiszlw.dcc.iface.constant.Env;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-04-26
 */
@Configuration
public class DccClientConfig {

    @Bean(initMethod = "init", destroyMethod = "destroy")
    public DccClient dccConfigClient() {
        DccClient dccClient = new DccClient();
        dccClient.setApplication("dcc-demo");
        dccClient.setEnv(Env.TEST);
        // 可不配，默认取default分组配置
        dccClient.setGroup("default");
        return dccClient;
    }
}
