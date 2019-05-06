package lewiszlw.dcc.server.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lewiszlw.dcc.server.exception.SerializationException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-05-06
 */
@Slf4j
public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String toJson(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error("JSON序列化异常", e);
            throw new SerializationException(e.getMessage(), e.getCause());
        }
    }

    public static <T> T fromJson(String json, Class<T> t) {
        try {
            return objectMapper.readValue(json, t);
        } catch (IOException e) {
            log.error("JSON反序列化异常", e);
            throw new SerializationException(e.getMessage(), e.getCause());
        }
    }
}
