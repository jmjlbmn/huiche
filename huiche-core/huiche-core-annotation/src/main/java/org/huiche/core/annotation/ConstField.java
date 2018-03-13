package org.huiche.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 常量类 值注解,用于常量工具类ConstUtil获取常量上注解的值
 *
 * @author Maning
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConstField {
    /**
     * 值,一般放置说明
     */
    String value() default "";

    /**
     * 扩展值,自行定义和取值
     */
    String extra() default "";

}
