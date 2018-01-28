package org.huiche.core.annotation;

import java.lang.annotation.*;

/**
 * @author Manig
 * @date 2018/1/20
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
    /**
     * 表名
     */
    String value() default "";

    /**
     * 表注释
     */
    String comment() default "";

    /**
     * 编码
     */
    String charset() default "utf8mb4";

    /**
     * 数据库引擎
     */
    String engine() default "InnoDB";
}
