package lewiszlw.dcc.server.config;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-04-26
 */
@Configuration
@EnableDubbo(scanBasePackages = "lewiszlw.dcc.server.dubbo")
@PropertySource("classpath:/spring/dubbo-provider.properties")
public class DubboProviderConfig {

}
