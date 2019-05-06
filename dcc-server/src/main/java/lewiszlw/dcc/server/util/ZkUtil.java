package lewiszlw.dcc.server.util;

import lewiszlw.dcc.iface.constant.Env;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-04-29
 */
public class ZkUtil {

    private static final String PATH_SEPARATOR = "/";
    private static final String PATH_ROOT = "/dcc";

    public static String path(String application, Env env, String group, String key) {
        StringBuilder path = new StringBuilder();
        path.append(PATH_ROOT)
                .append(PATH_SEPARATOR).append(application)
                .append(PATH_SEPARATOR).append(env)
                .append(PATH_SEPARATOR).append(group)
                .append(PATH_SEPARATOR).append(key);
        return path.toString();
    }

    public static String path(String application, Env env, String group) {
        StringBuilder path = new StringBuilder();
        path.append(PATH_ROOT)
                .append(PATH_SEPARATOR).append(application)
                .append(PATH_SEPARATOR).append(env)
                .append(PATH_SEPARATOR).append(group);
        return path.toString();
    }

}
