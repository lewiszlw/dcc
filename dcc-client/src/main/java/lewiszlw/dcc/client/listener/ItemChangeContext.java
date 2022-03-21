package lewiszlw.dcc.client.listener;

import lewiszlw.dcc.iface.constant.Env;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemChangeContext {
    private String application;
    private Env env;
    private String key;
    /** config旧值 */
    private String oldValue;
    /** config新值 */
    private String newValue;
}
