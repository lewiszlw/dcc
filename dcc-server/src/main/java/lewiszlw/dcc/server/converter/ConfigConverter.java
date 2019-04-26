package lewiszlw.dcc.server.converter;

import lewiszlw.dcc.iface.response.ConfigDTO;
import lewiszlw.dcc.server.entity.ConfigEntity;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-04-26
 */
public class ConfigConverter {

    public static ConfigDTO configEntityToConfigDTO(ConfigEntity configEntity) {
        return new ConfigDTO()
                .setApplication(configEntity.getApplication())
                .setEnv(configEntity.getEnv())
                .setGroup(configEntity.getGroup())
                .setKey(configEntity.getKey())
                .setVersion(configEntity.getVersion())
                .setValue(configEntity.getValue())
                .setComment(configEntity.getComment());
    }
}
