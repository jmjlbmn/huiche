package org.huiche.core.annotation.consts;

import java.lang.annotation.*;

/**
 * 常量类 值注解,用于常量工具类ConstUtil获取常量上注解的值
 *
 * @author Maning
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConstVal {
    /**
     * 值,一般放置说明
     * @return 值
     */
    String value() default "";

    /**
     * 扩展值,自行定义和取值
     * @return 值
     */
    String extra() default "";

}
