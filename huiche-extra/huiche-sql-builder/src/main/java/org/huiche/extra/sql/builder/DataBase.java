package org.huiche.extra.sql.builder;


import lombok.extern.slf4j.Slf4j;
import org.huiche.extra.sql.builder.sql.Mysql;
import org.huiche.extra.sql.builder.sql.Sql;

import javax.annotation.Nonnull;

/**
 * 已实现支持的数据库
 *
 * @author Maning
 */
@Slf4j
public enum DataBase {
    /**
     * MySql数据库
     */
    MYSQL(Mysql.sql(), "jdbc:mysql");

    private final Sql sql;
    private final String prefix;

    DataBase(@Nonnull Sql sql, @Nonnull String prefix) {
        this.sql = sql;
        this.prefix = prefix;
    }

    /**
     * 初始化
     *
     * @param url JDBC URL
     * @return 数据库
     */
    @Nonnull
    public static DataBase init(@Nonnull String url) {
        DataBase dataBase = null;
        for (DataBase db : DataBase.values()) {
            if (url.startsWith(db.prefix)) {
                dataBase = db;
                break;
            }
        }
        if (null == dataBase) {
            throw new RuntimeException("请检查,无法解析您的JDBC URL: " + url);
        }
        return dataBase;
    }

    public Sql sql() {
        return sql;
    }
}
