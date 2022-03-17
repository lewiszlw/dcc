package lewiszlw.dcc.server.util;

import lewiszlw.dcc.iface.constant.Env;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-04-29
 */
public class ZkUtil {

    public static final String PATH_SEPARATOR = "/";
    public static final String PATH_ROOT = "/dcc";

    public static String configPath(String application, Env env, String key) {
        StringBuilder path = new StringBuilder();
        path.append(PATH_ROOT)
                .append(PATH_SEPARATOR).append(application)
                .append(PATH_SEPARATOR).append(env)
                .append(PATH_SEPARATOR).append(key);
        return path.toString();
    }

    public static String path(String application, Env env) {
        StringBuilder path = new StringBuilder();
        path.append(PATH_ROOT)
                .append(PATH_SEPARATOR).append(application)
                .append(PATH_SEPARATOR).append(env);
        return path.toString();
    }

}
