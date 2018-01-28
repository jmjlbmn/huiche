package org.huiche.core.consts;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 常量类 值注解
 *
 * @author Maning
 * @date 2017/3/2
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConstField {
    String value() default "";

    String extra() default "";

}
