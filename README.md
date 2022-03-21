# 项目简介
DCC (Distributed Config Center)，一个轻量级的分布式配置中心。

TODO
- [x] dccClient.get("xxx") 实时获取配置
- [x] @DccConfig 注解来获取配置
- [x] 容灾能力（dcc-server宕机不影响客户端应用程序正常启动和运行）
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
1. 安装 Zookeeper 和 MySQL
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

**2. 如何实现配置实时推送更新机制？**

**3. 如何实现容灾能力？**

**4. 支持哪些数据类型的配置**
