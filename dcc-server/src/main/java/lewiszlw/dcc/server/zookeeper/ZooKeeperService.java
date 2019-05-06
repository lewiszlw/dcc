package lewiszlw.dcc.server.zookeeper;

import lewiszlw.dcc.server.exception.ZooKeeperException;
import lewiszlw.dcc.server.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;

/**
 * Desc: zookeeper服务
 *
 * @author zhanglinwei02
 * @date 2019-04-24
 */
@Slf4j
@Service
public class ZooKeeperService implements InitializingBean {

    @Value("${zookeeper.url}")
    private String zkURL;

    private ZooKeeper zookeeper;

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }

    /**
     * 初始化
     */
    private void init() throws IOException {
        zookeeper = new ZooKeeper(zkURL, 5000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                if (event.getState() == Event.KeeperState.SyncConnected) {
                    log.info("Zookeeper SyncConnected");
                } else {
                    log.error("Zookeeper is inactive");
                }
            }
        });
    }

    /**
     * 获取zookeeper连接状态
     */
    public ZooKeeper.States state() {
        return zookeeper.getState();
    }

    /**
     * 创建节点
     * @return 返回实际路径
     */
    public String create(String path, String value) {
        if (StringUtils.isEmpty(value)) {
            throw new IllegalArgumentException("value is blank");
        }
        try {
            return zookeeper.create(path, value.getBytes("UTF-8"),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } catch (Exception e) {
            log.error("create path={}, value={} fail", path, value, e);
            throw new ZooKeeperException(e.getMessage(), e.getCause());
        }
    }

    /**
     * 判断节点是否存在
     */
    public boolean exists(String path) {
        try {
            Stat stat = zookeeper.exists(path, false);
            return stat == null? false: true;
        } catch (Exception e) {
            log.error("exists path={} fail", path, e);
            throw new ZooKeeperException(e.getMessage(), e.getCause());
        }
    }

    /**
     * 获取节点值
     */
    public String get(String path) {
        try {
            byte[] data = zookeeper.getData(path, false, null);
            return new String(data);
        } catch (Exception e) {
            log.error("get path={} fail", path, e);
            throw new ZooKeeperException(e.getMessage(), e.getCause());
        }
    }

    /**
     * 设置节点值
     */
    public void set(String path, String value) {
        try {
            zookeeper.setData(path, value.getBytes("UTF-8"), -1);
        } catch (Exception e) {
            log.error("set path={}, value={} fail", path, value, e);
            throw new ZooKeeperException(e.getMessage(), e.getCause());
        }
    }

    /**
     * 获取子节点
     */
    public List<String> getChildren(String path) {
        try {
            log.info("ZooKeeperService.getChildren 请求参数: path={}", path);
            List<String> children = zookeeper.getChildren(path, false);
            log.info("ZooKeeperService.getChildren 请求响应: children={}", JsonUtil.toJson(children));
            return children;
        } catch (Exception e) {
            log.error("getChildren path={} fail", path, e);
            throw new ZooKeeperException(e.getMessage(), e.getCause());
        }
    }

    /**
     * 删除节点
     */
    public void delete(String path) {
        try {
            // -1直接删除
            zookeeper.delete(path, -1);
        } catch (Exception e) {
            log.error("delete path={} fail", path, e);
            throw new ZooKeeperException(e.getMessage(), e.getCause());
        }
    }

    /**
     * 释放连接
     */
    public void release() {
        try {
            zookeeper.close();
        } catch (InterruptedException e) {
            log.error("zookeeper close fail", e);
            throw new ZooKeeperException(e.getMessage(), e.getCause());
        }
    }
}
