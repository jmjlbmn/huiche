package org.huiche.core.annotation;

import java.lang.annotation.*;

/**
 * @author Manig
 * @date 2018/1/20
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    /**
     * 字段名
     */
    String value() default "";

    /**
     * 是否是主键
     */
    boolean isPrimaryKey() default false;

    /**
     * 长度
     */
    int length() default 0;

    /**
     * 精度
     */
    int precision() default 0;

    /**
     * 是否唯一
     * zan
     */
    boolean unique() default false;

    /**
     * 是否可以是空
     */
    boolean notNull() default false;

    /**
     * 是否是数据库字段
     */
    boolean isDbField() default true;

    /**
     * 列注释
     */
    String comment() default "";
}
