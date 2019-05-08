package lewiszlw.dcc.server.mapper;

import com.google.common.collect.Lists;
import lewiszlw.dcc.iface.constant.Env;
import lewiszlw.dcc.server.constant.Constants;
import lewiszlw.dcc.server.entity.ConfigEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-05-08
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ConfigMapperTest {

    @Autowired
    private ConfigMapper configMapper;

    @Test
    public void test() {
        // TODO 待测试
        String application = "test-app121";
        String key = "test-key";
        String value = "test-value";
        String comment = "test-comment";
        ConfigEntity configEntity = new ConfigEntity().setApplication(application)
                                                        .setEnv(Env.TEST)
                                                        .setGroup(Constants.DEFAULT_GROUP)
                                                        .setKey(key)
                                                        .setVersion(Constants.INIT_VERSION)
                                                        .setValue(value)
                                                        .setComment(comment);
        Assert.assertTrue(configMapper.batchInsert(Lists.newArrayList(configEntity)) == 1);
        List<ConfigEntity> configEntities = configMapper.selectOneAllVersions("test-app", Env.TEST, Constants.DEFAULT_GROUP, "test-key");
        Assert.assertTrue(configEntities.size() == 1);
        ConfigEntity configEntityFromDB = configEntities.get(0);
        Assert.assertTrue(configEntityFromDB.getApplication().equals(application));
        Assert.assertTrue(configEntityFromDB.getEnv().equals(Env.TEST));
        Assert.assertTrue(configEntityFromDB.getGroup().equals(Constants.DEFAULT_GROUP));
        Assert.assertTrue(configEntityFromDB.getKey().equals(key));
        Assert.assertTrue(configEntityFromDB.getVersion() == Constants.INIT_VERSION);
        Assert.assertTrue(configEntityFromDB.getComment().equals(comment));

        // 删除测试数据
        configMapper.batchDelete(configEntities.stream().map(ConfigEntity::getId).collect(Collectors.toList()));
    }
}
