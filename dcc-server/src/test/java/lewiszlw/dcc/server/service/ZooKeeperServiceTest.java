package lewiszlw.dcc.server.service;

import lewiszlw.dcc.server.zookeeper.ZooKeeperService;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-04-24
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ZooKeeperServiceTest {

    @Autowired
    private ZooKeeperService zooKeeperService;

    @Test
    public void testCreate() {
        zooKeeperService.create("/config1", "hello");
        Assert.assertTrue("hello".equals(zooKeeperService.get("/config1")));
        zooKeeperService.delete("/config1");
        Assert.assertTrue(!zooKeeperService.exists("/config1"));
    }

    @Test
    public void testExists() {
        if (zooKeeperService.exists("/config2")) {
            zooKeeperService.delete("/config2");
        }
        zooKeeperService.delete("/config2");
        zooKeeperService.create("/config2", "world");
        Assert.assertTrue(zooKeeperService.exists("/config2"));
        Assert.assertTrue(!zooKeeperService.exists("/config1"));
        zooKeeperService.delete("/config2");
    }

    @Test
    public void testGet() {
        if (zooKeeperService.exists("/test-config")) {
            zooKeeperService.delete("/test-config");
        }
        zooKeeperService.create("/test-config", "hello");
        Assert.assertTrue("hello".equals(zooKeeperService.get("/test-config")));
        zooKeeperService.delete("/test-config");
    }

    @Test
    public void testSet() {
        if (zooKeeperService.exists("/test-config")) {
            zooKeeperService.delete("/test-config");
        }
        zooKeeperService.create("/test-config", "hello");
        zooKeeperService.set("/test-config", "world");
        Assert.assertTrue("world".equals(zooKeeperService.get("/test-config")));
        zooKeeperService.delete("/test-config");
    }

    @Test
    public void testDelete() {
        if (zooKeeperService.exists("/test-config")) {
            zooKeeperService.delete("/test-config");
        }
        Assert.assertTrue(!zooKeeperService.exists("/test-config"));
        zooKeeperService.create("/test-config", "hello");
        Assert.assertTrue(zooKeeperService.exists("/test-config"));
        zooKeeperService.delete("/test-config");
        Assert.assertTrue(!zooKeeperService.exists("/test-config"));
    }

    @Test
    public void testRelease() {
        Assert.assertTrue(zooKeeperService.state() == ZooKeeper.States.CONNECTED);
        zooKeeperService.release();
        Assert.assertTrue(zooKeeperService.state() == ZooKeeper.States.CLOSED);
    }

    @Test
    public void testGetChildren() {
        if (!zooKeeperService.exists("/test-config")) {
            zooKeeperService.create("/test-config", "null");
        }
        if (!zooKeeperService.exists("/test-config/cfg1")) {
            zooKeeperService.create("/test-config/cfg1", "null");
        }
        if (!zooKeeperService.exists("/test-config/cfg2")) {
            zooKeeperService.create("/test-config/cfg2", "null");
        }
        List<String> children = zooKeeperService.getChildren("/test-config");
        Assert.assertTrue(children.size() == 2);
        Assert.assertTrue(children.contains("cfg1"));
        Assert.assertTrue(children.contains("cfg2"));

        zooKeeperService.delete("/test-config/cfg1");
        zooKeeperService.delete("/test-config/cfg2");
        zooKeeperService.delete("/test-config");
    }
}
