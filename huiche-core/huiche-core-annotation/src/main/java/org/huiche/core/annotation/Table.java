package org.huiche.core.annotation;

import java.lang.annotation.*;

/**
 * 用于SqlBuilder生成数据库的实体类注解,必须设置,不然不会当做表
 *
 * @author Maning
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
