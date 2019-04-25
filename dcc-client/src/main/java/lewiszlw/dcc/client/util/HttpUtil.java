package lewiszlw.dcc.client.util;

import com.google.common.io.CharStreams;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-04-25
 */
public class HttpUtil {

    public String get(String url, int connectTimeOut, int readTimeOut) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(connectTimeOut);
        connection.setReadTimeout(readTimeOut);

        connection.connect();

        int responseCode = connection.getResponseCode();

        InputStreamReader isr = new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8"));
        return CharStreams.toString(isr);
    }
}
