package lewiszlw.dcc.client.listener;

import lewiszlw.dcc.iface.constant.Env;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpaceChangeContext {
    private String application;
    private Env env;
    /** 发生改变的config keys */
    private List<String> changedKeys;
}
