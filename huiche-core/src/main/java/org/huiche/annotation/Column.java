package org.huiche.annotation;

import org.huiche.support.IdGenerator;
import org.huiche.support.PrimaryKey;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.JDBCType;

/**
 * 标注属性/字段的注解
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
    String name() default "";

    /**
     * 列注释
     *
     * @return 值
     */
    String comment() default "";

    /**
     * 是否允许空值
     *
     * @return 值
     */
    boolean nullable() default true;

    /**
     * 长度,仅字符串或小数时生效,int/long/boolean无效,默认255
     *
     * @return 值
     */
    int length() default -1;

    /**
     * 精度,仅小数时生效
     *
     * @return 值
     */
    int precision() default -1;

    boolean unique() default false;

    /**
     * @return jdbc类型
     */
    JDBCType jdbcType() default JDBCType.NULL;

    String defaultValue() default "";

    PrimaryKey primaryKey() default PrimaryKey.NOT_PK;

    Class<? extends IdGenerator> idGenerator() default IdGenerator.class;

    String additional() default "";

    boolean unsigned() default false;

    String definition() default "";

}
