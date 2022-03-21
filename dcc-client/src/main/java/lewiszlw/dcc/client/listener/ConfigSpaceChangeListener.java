package lewiszlw.dcc.client.listener;

/**
 * 监听应用所有配置修改事件
 */
public interface ConfigSpaceChangeListener {
    void process(SpaceChangeContext context);
}
