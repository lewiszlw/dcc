package lewiszlw.dcc.demo;

import lewiszlw.dcc.client.config.DccDubboConsumerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(DccDubboConsumerConfig.class)
public class DccDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DccDemoApplication.class, args);
    }

}
