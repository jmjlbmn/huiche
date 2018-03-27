package org.huiche.core.annotation.search;

import java.lang.annotation.*;

/**
 * 用于QueryDsl筛选的类
 *
 * @author Maning
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SearchColumn {
    /**
     * 列名
     *
     * @return 列名
     */
    String value() default "";

    /**
     * 表名
     *
     * @return 表名
     */
    String table() default "";

    /**
     * 操作符
     *
     * @return 操作符
     */
    Op operator() default Op.EQ;

    /**
     * 是否取反
     * @return 取反
     */
    boolean not() default false;

    /**
     * 操作符
     */
    enum Op {
        /**
         * 等于
         */
        EQ,
        /**
         * 小于
         */
        LT,
        /**
         * 小于等于
         */
        LOE,
        /**
         * 大于
         */
        GT,
        /**
         * 小于
         */
        GOE,
        /**
         * is null
         */
        IS_NULL,
        /**
         * trim = ""
         */
        IS_EMPTY,
        /**
         * like,规则: '*'代替sql中的%,匹配0~多个字符, '_'等同sql中的_,匹配1个字符,如果未传入*或_,默认会在两边加入%
         */
        LIKE,
        /**
         * like并忽略大小写,规则同like
         */
        LIKE_IGNORE_CASE,
        /**
         * in,条目以逗号分隔
         */
        IN
    }
}
