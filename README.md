# 项目简介
DCC (Distributed Config Center)，一个轻量级的分布式配置中心。

TODO
- [x] dccClient.get("xxx") 实时获取配置
- [x] @DccConfig 注解来获取配置
- [x] 容灾能力（dcc-server宕机不影响客户端应用程序正常启动和运行）
- [x] 多版本配置
- [x] 配置实时推送更新
- [x] 配置定时拉取
- [ ] 支持监听配置修改事件
- [ ] 配置灰度发布
- [ ] 支持回滚配置
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
```

# FAQ
**1. group 是做什么的？**