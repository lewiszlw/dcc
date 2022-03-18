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
        // 扫描包路径，默认扫描全部
        // dccClient.setScanBasePackages(".");
        // 本地缓存持久化文件路径，默认为/opt/dcc/cache，需提前赋予读写权限
        // dccClient.setCacheFilePath("/opt/dcc/cache");
        // 定时全量拉取周期，默认180秒拉取一次
        // dccClient.setPeriod(180);
        dccClient.setPeriod(60);
        return dccClient;
    }
}
