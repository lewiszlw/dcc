package lewiszlw.dcc.server.vo;

import lewiszlw.dcc.iface.constant.Env;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RollbackRequest {
    private String application;
    private Env env;
    private String key;
    private int targetVersion;
}
