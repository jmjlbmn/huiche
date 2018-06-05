package org.huiche.extra.sql.builder.naming;

import javax.annotation.Nonnull;

/**
 * sql命名规则接口
 *
 * @author Maning
 */
public interface NamingRule {
    /**
     * java名称(类,属性)转数据库(表,字段)名称
     *
     * @param javaName Java名称
     * @return 数据库名称
     */
    @Nonnull
    String javaName2SqlName(@Nonnull String javaName);

    /**
     * java名称(类,属性)转数据库(表,字段)名称
     *
     * @param sqlName 数据库名称
     * @return java名称
     */
    @Nonnull
    String sqlName2JavaName(@Nonnull String sqlName);
}
