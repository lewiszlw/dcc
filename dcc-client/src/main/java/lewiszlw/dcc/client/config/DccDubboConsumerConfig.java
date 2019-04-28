package lewiszlw.dcc.client.config;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-04-26
 */
@Configuration
@EnableDubbo(scanBasePackages = "lewiszlw.dcc.client")
@PropertySource("classpath:/spring/dubbo-consumer.properties")
@ComponentScan(value = {"lewiszlw.dcc.client"})
public class DccDubboConsumerConfig {
}
