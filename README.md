# 项目简介
DCC (Distributed Config Center)，一个轻量级的分布式配置中心。

# 开始
## 服务端
1.安装zookeeper

2.启动dcc-server

## 客户端
1.引入maven依赖
```xml
<dependency>
    <groupId>lewiszlw</groupId>
    <artifactId>dcc-client</artifactId>
    <version>0.1.1-SNAPSHOT</version>
</dependency>
```
2.配置DccClient
```java
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
```
3.配置文件application.properties
```
# dubbo
dubbo.application.name=dubbo-demo
dubbo.registry.address=zookeeper://127.0.0.1:2181
dubbo.consumer.timeout=3000
```
4.启动类引入DubboConsumerConfig
```java
@SpringBootApplication
@Import(DubboConsumerConfig.class)
public class DccDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DccDemoApplication.class, args);
    }

}
```
4.使用DccClient动态配置
```java
// TODO
```

# 功能
- 配置监听
- 配置回滚
- 配置修改日志
- 权限管理
- 容灾：本地缓存

# 模块说明
- dcc-server: 配置中心服务端
- dcc-interface: dubbo服务接口定义
- dcc-client: 配置中心客户端sdk
- dcc-demo: 客户端演示