![build-status](https://travis-ci.com/lewiszlw/dcc.svg?branch=master)
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

    @Bean(initMethod = "init", destroyMethod = "destroy")
    public DccClient dccConfigClient() {
        DccClient dccClient = new DccClient();
        dccClient.setApplication("dcc-demo");
        dccClient.setEnv(Env.TEST);
        // 分组配置，默认取default
        // dccClient.setGroup("default");
        // 扫描包路径，默认扫描全部
        // dccClient.setScanBasePackages(".");
        // 本地缓存持久化文件路径，默认为/opt/dcc/cache，需提前赋予读写权限
        // dccClient.setCacheFilePath("/opt/dcc/cache");
        // 定时全量拉取周期，默认180秒拉取一次
        // dccClient.setPeriod(180);
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
// 1.使用client
@Autowired
private DccClient dccClient;

dccClient.get("configKey");

// 2.使用注解
@DccConfig
private static String configKey1; // key为变量名

@DccConfig(key = "configKey2")
private static String config;
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