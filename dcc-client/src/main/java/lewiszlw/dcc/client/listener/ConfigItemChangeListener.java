package lewiszlw.dcc.client.listener;

/**
 * 监听单个配置修改事件
 */
public interface ConfigItemChangeListener {
    void process(ItemChangeContext context);
}
