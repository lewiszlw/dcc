package lewiszlw.dcc.server.annotation;

import lewiszlw.dcc.server.constant.RecordType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Desc: 记录注解
 *
 * @author zhanglinwei02
 * @date 2019-04-24
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Record {
    /**
     * 记录类型
     */
    RecordType type();

}
