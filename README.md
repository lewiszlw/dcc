# 项目简介
DCC (Distributed Config Center)，一个轻量级的分布式配置中心。

TODO
- [x] dccClient.get("xxx") 实时获取配置
- [x] @DccConfig 注解来获取配置
- [x] 客户端容灾能力
- [x] 多版本配置
- [x] Server端实时推送配置更新
- [x] Client端定时拉取所有最新配置
- [x] 支持回滚配置
- [x] 支持多种数据类型配置
- [x] 支持监听配置修改事件
- [ ] 配置灰度发布
- [ ] 配置修改权限控制

# 开始
## 模块说明
- dcc-server: 配置中心服务端
- dcc-interface: 配置中心服务接口定义
- dcc-client: 配置中心客户端sdk
- dcc-demo: 客户端演示

## 服务端
1. 安装 Zookeeper 和 MySQL （可参考 docker-compose.yml 启动容器）
2. 执行 sql/create_tables.sql
3. 在 application.properties 配置 Zookeeper 和 MySQL 
4. 运行 DccServerApplication 启动 dcc-server

## 客户端SDK
1. 将 DccClient.ZK_URL 配置与服务端 ZK 地址保持一致

## 客户端应用程序
1. 创建 /opt/dcc/cache 文件并赋予权限 `sudo chmod -R 777 /opt/dcc`
2. 引入 sdk dcc-client maven依赖
3. 配置 DccClient bean （见 DccClientConfig.java）
4. 启动类 DccDemoApplication 引入 DubboConsumerConfig
5. 使用动态配置
```java
// 1.使用client
@Autowired
private DccClient dccClient;

dccClient.get("configKey");
dccClient.getAll();

// 2.使用注解
@DccConfig
private static String configKey1; // key为变量名

@DccConfig(key = "configKey2")
private static String config;

// 3.支持多种数据结构
@DccConfig
private static int configIntKey;
@DccConfig
private static Long configLongKey;
@DccConfig
private static float configFloatKey;
@DccConfig
private static Double configDoubleKey;
@DccConfig
private static Student configObjKey;
@DccConfig
private static Map<String, Student> configMapKey;
@DccConfig
private static List<Student> configListKey;

// 4.注册监听器
dccClient.registerListener(context -> log.info("ConfigSpaceChangeListener received event, context: {}", context));
dccClient.registerListener("configKey1", context -> log.info("ConfigItemChangeListener received event, context: {}", context));
```

# FAQ
**1. application、env、group 和 key 是做什么的？**

application 是应用名称，全局唯一。env 是应用的环境。key 是配置名称，key 在 application-env 下唯一。
group 仅作为前端展示时对 key 分组展示，以避免应用程序有大量配置时，UI展示不友好。

**2. 如何实现配置实时推送更新机制？**

通过 Zookeeper 的监听机制。Zookeeper 中会按照 /dcc/{application}/{env}/{key} 形式存放最新版本的配置，
服务端每次更新配置时，会将最新配置更新到 Zookeeper 相应的节点上。客户端 SDK 会在客户端应用启动时，
往客户端配置空间（即 /dcc/{application}/{env} ）注册监听器，监听所有子节点变化，当收到子节点更新事件时，
会即时更新配置缓存和注解字段。

**3. 如何实现客户端容灾能力？**

客户端容灾能力是指 
1. dcc-server 宕机不影响客户端应用程序正常启动：每次配置更新时，会将缓存的配置全部写入到本地磁盘文件（如果是容器部署，这里可重构成分布式文件存储服务）， 
当客户端应用启动时，如果发现从 dcc-server 无法获取所有配置，则会从本地文件读取之前写入的配置。
2. dcc-server 突然宕机不影响客户端应用程序运行：客户端 SDK 会缓存所有配置，dcc-server 突然宕机时，SDK 中的缓存依然可以继续支持客户端应用读取配置。

**4. 支持哪些数据类型的配置，如何支持的？**

支持 String、int/Integer、long/Long、float/Float、double/Double、List、Map、Object 类型。服务端统一存放字符串类型，
客户端 SDK 拿到配置字符串后，通过反射拿到客户端应用配置的数据类型，进行字符串解析或反序列化。

**5. 配置如何存储的？**

所有版本的配置（包含最新）均会存放在 MySQL，客户端全量拉取所有配置时，通过读取 MySQL 来获取所有最新配置。
Zookeeper 中仅存放每个配置的最新内容，当配置改动时，即时推送给客户端最新配置。

**6. 客户端应用与配置中心服务端是如何通信的？**

目前采用 Dubbo 进行通信，Dubbo 配置封装在客户端 SDK 中。后续可以支持 http 之类的更为通用的通信方式。

**7. 客户端应用配置更新机制？**

采取推拉结合方式，服务端通过 Zookeeper 监听机制来将最新配置推送给客户端应用，客户端应用也会通过 SDK 来定时全量拉取所有最新配置。