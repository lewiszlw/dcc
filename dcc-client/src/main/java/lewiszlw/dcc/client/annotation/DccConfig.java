package lewiszlw.dcc.client.annotation;

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
     * 配置项key
     */
    String key() default "";
}
