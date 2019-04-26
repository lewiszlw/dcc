package lewiszlw.dcc.server.zookeeper.watcher;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-04-24
 */
@Slf4j
public class ConnectionWatcher implements Watcher {



    public void process(WatchedEvent event) {
        switch (event.getState()) {
            case SyncConnected:
                log.info("zookeeper SyncConnected");
                break;
            case AuthFailed:
                log.error("zookeeper AuthFailed");
                break;
            case Disconnected:
                log.warn("zookeeper Disconnected");
                break;
            case Expired:
                log.warn("zookeeper Expired");
                reconnect();
                break;
        }
    }

    private void reconnect() {

    }
}
