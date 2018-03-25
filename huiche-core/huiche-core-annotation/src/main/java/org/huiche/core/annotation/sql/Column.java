package org.huiche.core.annotation.sql;

import java.lang.annotation.*;

/**
 * 用于SqlBuilder生成数据库表的字段/列的实体类属性的注解
 *
 * @author Maning
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    /**
     * 字段名
     *
     * @return 值
     */
    String value() default "";

    /**
     * 是否是主键
     *
     * @return 值
     */
    boolean isPrimaryKey() default false;

    /**
     * 是否自增,仅是主键时生效
     *
     * @return 值
     */
    boolean isAutoIncrement() default true;

    /**
     * 长度,仅字符串或小数时生效,int/long/boolean无效
     *
     * @return 值
     */
    int length() default 0;

    /**
     * 精度,仅小数时生效
     *
     * @return 值
     */
    int precision() default 0;

    /**
     * 是否唯一
     * 暂时只支持唯一索引的创建,不支持修改,即不能删除(不安全)
     *
     * @return 值
     */
    boolean unique() default false;

    /**
     * 是否可以是空
     *
     * @return 值
     */
    boolean notNull() default false;

    /**
     * 是否是数据库字段
     *
     * @return 值
     */
    boolean isDbField() default true;

    /**
     * 列注释
     *
     * @return 值
     */
    String comment() default "";
}
