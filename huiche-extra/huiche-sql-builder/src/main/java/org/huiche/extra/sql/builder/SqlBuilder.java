package org.huiche.extra.sql.builder;

import lombok.extern.slf4j.Slf4j;
import org.huiche.annotation.sql.Table;
import org.huiche.extra.sql.builder.info.ColumnCompareInfo;
import org.huiche.extra.sql.builder.info.ColumnInfo;
import org.huiche.extra.sql.builder.info.TableInfo;
import org.huiche.extra.sql.builder.naming.CamelCaseNamingRule;
import org.huiche.extra.sql.builder.naming.NamingRule;
import org.huiche.extra.sql.builder.sql.Sql;
import org.reflections.Reflections;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

/**
 * 建表工具入口类
 *
 * @author Maning
 */
@Slf4j
public class SqlBuilder {
    private String url;
    private String user;
    private String password;
    private Sql dbSql;
    private NamingRule namingRule;
    private List<String> sqlList;
    private List<String> manualSqlList;

    private SqlBuilder() {
    }

    /**
     * 初始化方法
     *
     * @param jdbcUrl  JDBC_URL
     * @param user     数据库用户名
     * @param password 数据库密码
     * @return builder
     */
    public static SqlBuilder init(@Nonnull String jdbcUrl, @Nonnull String user, @Nonnull String password) {
        return init(jdbcUrl, user, password, CamelCaseNamingRule.getInstance(), null);
    }

    /**
     * 初始化方法
     *
     * @param jdbcUrl    JDBC_URL
     * @param user       数据库用户名
     * @param password   数据库密码
     * @param namingRule 命名规则
     * @param sql        数据库
     * @return builder
     */
    public static SqlBuilder init(@Nonnull String jdbcUrl, @Nonnull String user, @Nonnull String password, @Nullable NamingRule namingRule, @Nullable Sql sql) {
        SqlBuilder builder = new SqlBuilder();
        builder.url = jdbcUrl;
        builder.user = user;
        builder.password = password;
        if (null == namingRule) {
            builder.namingRule = CamelCaseNamingRule.getInstance();
        }
        builder.namingRule = namingRule;
        if (null == sql) {
            builder.dbSql = DataBase.init(builder.url).sql();
        } else {
            builder.dbSql = sql;
        }
        builder.sqlList = new ArrayList<>();
        builder.manualSqlList = new ArrayList<>();
        return builder;
    }

    public void run(@Nonnull String packageName) {
        run(false, packageName);
    }

    public void run(boolean update, @Nonnull String packageName) {
        try {
            run(update, new Reflections(packageName).getTypesAnnotatedWith(Table.class,true));
        } catch (Exception e) {
            logError(e);
        }
    }

    /**
     * 执行部分类
     *
     * @param classes 要执行的类
     */
    public void runSome(Class<?>... classes) {
        runSome(false, classes);
    }

    /**
     * 执行部分类
     *
     * @param update  是否进行更新
     * @param classes 要执行的类
     */
    public void runSome(boolean update, Class<?>... classes) {
        run(update, Arrays.asList(classes));
    }

    /**
     * 执行
     *
     * @param update  是否执行修改列和删除列操作
     * @param classes 表
     */
    public void run(boolean update, Collection<Class<?>> classes) {
        try {
            if (classes.size() == 0) {
                log.error("没有要生成SQL的类,不会进行操作");
                return;
            }
            sqlList.clear();
            manualSqlList.clear();
            Properties props = new Properties();
            props.setProperty("user", user);
            props.setProperty("password", password);
            props.setProperty("remarks", "true");
            props.setProperty("useInformationSchema", "true");
            try (Connection conn = DriverManager.getConnection(url, props)) {
                conn.setAutoCommit(true);
                create(classes, conn, update);
            } catch (Exception e) {
                printSql();
                throw new RuntimeException(e);
            }
            printSql();
        } catch (Exception e) {
            logError(e);
        }
    }

    private void logError(Throwable ex) {
        StringWriter sbw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sbw));
        log.error(sbw.toString());
    }

    private void create(@Nonnull Collection<Class<?>> classes, @Nonnull Connection conn, boolean update) {
        for (Class<?> clazz : classes) {
            TableInfo tableInfo = Sql.getInfo(clazz, namingRule);
            try {
                if (dbSql.checkTableExists(conn, tableInfo.getName())) {
                    log.info("数据表: " + tableInfo.getName() + " 已经存在,即将检查对比,尝试进行修改 ... ");
                    try {
                        update(tableInfo, conn, update);
                    } catch (SQLException e1) {
                        log.info("修改实体: " + clazz.getSimpleName() + " 的数据表 失败!!!!!!");
                        throw new RuntimeException(e1);
                    }
                } else {
                    log.info("创建实体: " + clazz.getSimpleName() + " 的数据表 ... 开始");
                    executeSql(conn, Sql.BR + dbSql.getCreate(tableInfo));
                    log.info("创建实体: " + clazz.getSimpleName() + " 的数据表 ... 成功!!!");
                }
            } catch (SQLException e) {
                log.info("失败");
                e.printStackTrace();
            }
        }
    }

    private void update(@Nonnull TableInfo tableInfo, @Nonnull Connection conn, boolean update) throws SQLException {
        if (!tableInfo.getComment().equals(dbSql.getTableComment(conn, tableInfo.getName()))) {
            log.info("修改表的注释,表: " + tableInfo.getName());
            executeSql(conn, dbSql.getAlterTableComment(tableInfo));
        }
        List<ColumnInfo> javaList = tableInfo.getColumnInfoList();
        List<ColumnInfo> dbList = Sql.getInfo(conn, tableInfo.getName());
        ColumnCompareInfo compare = Sql.compare(javaList, dbList);
        if (compare.isEmpty()) {
            log.info("数据表: " + tableInfo.getName() + " 没有变动,直接跳过 ...");
            return;
        }
        log.info(Sql.TAB + "修改表: " + tableInfo.getName() + " ... 开始");
        if (!compare.getAddList().isEmpty()) {
            log.info(Sql.TAB + Sql.TAB + "需要增加列:" + compare.getAddList() + " 开始执行==>");
            for (ColumnInfo columnInfo : compare.getAddList()) {
                log.info(Sql.TAB + Sql.TAB + "增加列: " + columnInfo.getName() + " ... ");
                executeSql(conn, dbSql.getAlterAddColumn(tableInfo.getName(), columnInfo));
            }
        }
        if (!compare.getDelList().isEmpty()) {
            log.info(Sql.TAB + Sql.TAB + "需要删除列:" + compare.getDelList() + " 开始执行==>");
            for (ColumnInfo columnInfo : compare.getDelList()) {
                String sql = dbSql.getDropColumn(tableInfo.getName(), columnInfo.getName());
                if (update) {
                    log.info(Sql.TAB + Sql.TAB + "删除列: " + columnInfo.getName() + " ... ");
                    executeSql(conn, sql);
                } else {
                    manualSqlList.add(Sql.BR + sql + ";" + Sql.BR);
                }
            }
        }
        if (!compare.getModifyList().isEmpty()) {
            log.info(Sql.TAB + Sql.TAB + "需要修改列:" + compare.getModifyList() + " 开始执行==>");
            for (ColumnInfo columnInfo : compare.getModifyList()) {
                String sql = dbSql.getAlterModifyColumn(tableInfo.getName(), columnInfo);
                if (update) {
                    log.info(Sql.TAB + Sql.TAB + "修改列: " + columnInfo.getName() + " ... ");
                    executeSql(conn, sql);
                } else {
                    manualSqlList.add(Sql.BR + sql + ";" + Sql.BR);
                }

            }
        }
        log.info(Sql.TAB + "修改表: " + tableInfo.getName() + " ... 结束");
    }

    private void executeSql(@Nonnull Connection conn, @Nonnull String sql) throws SQLException {
        sqlList.add(Sql.BR + sql + ";" + Sql.BR);
        try {
            conn.prepareStatement(sql).execute();
        } catch (SQLException e) {
            sqlList.add("#执行失败!!!!!!!!!!!!!!!!!!!" + Sql.BR);
            throw e;
        }
        sqlList.add("#执行成功==================>" + Sql.BR);
    }

    private void printSql() {
        if (!sqlList.isEmpty()) {
            StringBuilder builder = new StringBuilder(Sql.BR + "#=====所有已经被执行的SQL如下=====>:" + Sql.BR);
            for (String sql : sqlList) {
                builder.append(sql);
            }
            log.info(builder.toString());
        }
        if (!manualSqlList.isEmpty()) {
            StringBuilder builder = new StringBuilder(Sql.BR + "#=====需要您手动执行的SQL如下=====>:" + Sql.BR);
            for (String sql : manualSqlList) {
                builder.append(sql);
            }
            log.info(builder.toString());
        }
    }
}
