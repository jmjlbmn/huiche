package org.huiche.annotation.consts;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于常量类的常类属性的注解,用于常量工具类ConstUtil获取常量上注解的值
 *
 * @author Maning
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConstVal {
    /**
     * 值,一般放置说明文字
     *
     * @return 值
     */
    String value();

    /**
     * 扩展值,自行定义和取值
     *
     * @return 值
     */
    String extra() default "";

}
