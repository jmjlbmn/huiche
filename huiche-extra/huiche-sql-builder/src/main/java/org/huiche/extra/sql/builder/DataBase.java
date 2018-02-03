package org.huiche.extra.sql.builder;


import org.huiche.extra.sql.builder.sql.Mysql;
import org.huiche.extra.sql.builder.sql.Sql;

/**
 * @author Maning
 */
public enum DataBase {
    /**
     * MySql数据库
     */
    MYSQL(Mysql.sql(), "jdbc:mysql", "com.mysql.jdbc.Driver", "com.mysql.cj.jdbc.Driver");

    private Sql sql;
    private String prefix;
    private String[] driverClass;

    DataBase(Sql sql, String prefix, String... driverClass) {
        this.sql = sql;
        this.prefix = prefix;
        this.driverClass = driverClass;
    }

    public static DataBase init(String url) {
        if (null == url) {
            throw new RuntimeException("传入JDBC URL不能为null");
        }
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
                System.err.println("无法注册名为" + name + "的驱动,请确认已经引入相应数据库驱动jar包");
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
