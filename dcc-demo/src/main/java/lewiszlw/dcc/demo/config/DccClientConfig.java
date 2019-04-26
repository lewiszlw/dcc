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

    @Bean
    public DccClient dccConfigClient() {
        DccClient dccClient = new DccClient();
        dccClient.setApplication("dcc-demo");
        dccClient.setEnv(Env.TEST);
        return dccClient;
    }
}
