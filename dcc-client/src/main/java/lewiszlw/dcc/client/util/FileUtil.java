package lewiszlw.dcc.client.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-04-28
 */
@Slf4j
public class FileUtil {

    public static void createIfNotExists(File file) throws IOException {
        File dir = file.getParentFile();
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                log.error("创建文件目录 {} 失败，请检查是否拥有读写权限或文件路径是否正确", file.getAbsolutePath());
            }
        }
        if (!file.exists()) {
            if (!file.createNewFile()) {
                log.error("创建文件 {} 失败，请检查是否拥有读写权限或文件路径是否正确", file.getAbsolutePath());
            }
        }
    }
}
