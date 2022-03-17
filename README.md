# 项目简介
DCC (Distributed Config Center)，一个轻量级的分布式配置中心。

TODO
- [ ] dccClient.get("xxx") 实时获取配置
- [ ] @DccConfig 注解来获取配置
- [ ] 支持监听配置修改事件
- [ ] 配置修改历史 
- [ ] 配置灰度发布
- [ ] 支持回滚配置
- [ ] 配置修改权限控制
- [ ] 容灾能力（dcc-server宕机尽可能不影响应用程序）

# 开始
## 服务端
1. 安装 Zookeeper 和 MySQL
2. 执行 sql/create_tables.sql
3. 在 application.properties 配置 zookeeper 和 MySQL 
4. 运行 DccServerApplication 启动 dcc-server

## 应用程序
1. 创建 /opt/dcc/cache 文件并赋予权限 `sudo chmod -R 777 /opt/dcc`
2. 引入 dcc-client maven依赖
3. 配置 DccClient bean （见 DccClientConfig.java）
4. application.properties 配置 dubbo
5. 启动类 DccDemoApplication 引入 DubboConsumerConfig
6. 使用DccClient动态配置
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