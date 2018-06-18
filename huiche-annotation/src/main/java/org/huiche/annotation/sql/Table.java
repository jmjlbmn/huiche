package org.huiche.annotation.sql;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于建表工具生成数据库的实体类注解,必须设置,不然不会当做表
 *
 * @author Maning
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
    /**
     * 表名,默认会根据驼峰转换全小写字母下划线分隔的字段名
     *
     * @return 值
     */
    String value() default "";

    /**
     * 表注释
     *
     * @return 值
     */
    String comment() default "";

    /**
     * 编码
     * mysql默认 utf8mb4
     *
     * @return 值
     */
    String charset() default "";

    /**
     * 数据库引擎
     * mysql 默认 InnoDB
     *
     * @return 值
     */
    String engine() default "";

    /**
     * 数据库排序/比较 规则,默认不设置,按数据库默认
     *
     * @return 排序/比较规则
     */
    String collation() default "";


}
