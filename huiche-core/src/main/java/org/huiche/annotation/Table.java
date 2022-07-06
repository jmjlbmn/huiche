package org.huiche.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注实体类的表注解
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
    String name() default "";

    /**
     * 表注释
     *
     * @return 值
     */
    String comment() default "";

    /**
     * @return schema
     */
    String schema() default "";

    String additional() default "";

    boolean generateMapper() default true;

    boolean generateDao() default true;
}
