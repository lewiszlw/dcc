package lewiszlw.dcc.client;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Desc: 注解配置
 *
 * @author zhanglinwei02
 * @date 2019-04-24
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DccConfig {
    /**
     * 应用配置空间
     */
    String namespace();

    /**
     * 配置项key
     */
    String key();
}
