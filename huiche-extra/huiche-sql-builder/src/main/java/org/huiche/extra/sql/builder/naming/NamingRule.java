package org.huiche.extra.sql.builder.naming;

/**
 * sql命名规则
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
    String javaName2SqlName(String javaName);

    /**
     * java名称(类,属性)转数据库(表,字段)名称
     *
     * @param sqlName 数据库名称
     * @return java名称
     */
    String sqlName2JavaName(String sqlName);
}
