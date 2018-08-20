package org.huiche.extra.sql.builder;


import lombok.extern.slf4j.Slf4j;
import org.huiche.extra.sql.builder.sql.Mysql;
import org.huiche.extra.sql.builder.sql.Sql;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

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
    MYSQL(Mysql.sql(), "jdbc:mysql", Arrays.asList("com.mysql.jdbc.Driver", "com.mysql.cj.jdbc.Driver"));

    private final Sql sql;
    private final String prefix;
    private final List<String> driverClass;

    DataBase(@Nonnull Sql sql, @Nonnull String prefix, @Nonnull List<String> driverClass) {
        this.sql = sql;
        this.prefix = prefix;
        this.driverClass = driverClass;
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
        boolean ok = false;
        for (String name : dataBase.driverClass) {
            try {
                Class.forName(name);
                ok = true;
                break;
            } catch (ClassNotFoundException e) {
                log.error("无法注册名为" + name + "的驱动,请确认已经引入相应数据库驱动jar包");
            }
        }
        if (!ok) {
            throw new RuntimeException("请检查是否引入与传入url相匹配的数据库驱动jar包,url: " + url);
        }
        return dataBase;
    }

    public Sql sql() {
        return sql;
    }
}
