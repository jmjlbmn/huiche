package org.huiche.annotation.sql;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于建表工具生成数据库表的字段/列的实体类属性的注解
 *
 * @author Maning
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    /**
     * 字段名,默认会根据驼峰转换全小写字母下划线分隔的字段名
     *
     * @return 值
     */
    String value() default "";

    /**
     * 是否是主键,暂不支持多主键
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
     * 长度,仅字符串或小数时生效,int/long/boolean无效,默认255
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
     * 是否唯一.
     * 暂时只支持唯一索引的创建,不支持修改,也不能删除(不安全).
     * 建议小项目仅在Java实现唯一验证,不在数据库层做唯一
     *
     * @return 值
     */
    boolean unique() default false;

    /**
     * 是否允许空值.
     * 建议小项目仅在Java实现非空验证,不在数据库做非空处理
     *
     * @return 值
     */
    boolean notNull() default false;

    /**
     * 是否是数据库字段,false时,不会创建此字段
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
