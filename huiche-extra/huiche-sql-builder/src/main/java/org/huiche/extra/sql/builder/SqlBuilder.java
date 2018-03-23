package org.huiche.extra.sql.builder;

import org.huiche.core.annotation.Table;
import org.huiche.extra.sql.builder.info.ColumnCompareInfo;
import org.huiche.extra.sql.builder.info.ColumnInfo;
import org.huiche.extra.sql.builder.info.TableInfo;
import org.huiche.extra.sql.builder.naming.CamelCaseNamingRule;
import org.huiche.extra.sql.builder.naming.NamingRule;
import org.huiche.extra.sql.builder.sql.Sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @author Maning
 */
public class SqlBuilder {
    private String url;
    private String user;
    private String password;
    private String rootPath;
    private Sql dbSql;
    private NamingRule namingRule;
    private static final SqlBuilder TOOL = new SqlBuilder();
    private List<String> sqlList;
    private List<String> manualSqlList;

    public static SqlBuilder init(String jdbcUrl, String user, String password) {
        return init(jdbcUrl, user, password, null, CamelCaseNamingRule.getInstance(), null);
    }

    public static SqlBuilder init(String jdbcUrl, String user, String password, String rootPath) {
        return init(jdbcUrl, user, password, rootPath, CamelCaseNamingRule.getInstance(), null);
    }

    public static SqlBuilder init(String jdbcUrl, String user, String password, Sql sql) {
        return init(jdbcUrl, user, password, null, CamelCaseNamingRule.getInstance(), sql);
    }

    public static SqlBuilder init(String jdbcUrl, String user, String password, NamingRule namingRule) {
        return init(jdbcUrl, user, password, namingRule, null);
    }

    public static SqlBuilder init(String jdbcUrl, String user, String password, String rootPath, Sql sql) {
        return init(jdbcUrl, user, password, rootPath, CamelCaseNamingRule.getInstance(), sql);
    }

    public static SqlBuilder init(String jdbcUrl, String user, String password, String rootPath, NamingRule namingRule) {
        return init(jdbcUrl, user, password, rootPath, namingRule, null);
    }

    public static SqlBuilder init(String jdbcUrl, String user, String password, NamingRule namingRule, Sql sql) {
        return init(jdbcUrl, user, password, null, namingRule, sql);
    }

    public static SqlBuilder init(String jdbcUrl, String user, String password, String rootPath, NamingRule namingRule, Sql sql) {
        TOOL.url = jdbcUrl;
        TOOL.user = user;
        TOOL.password = password;
        TOOL.rootPath = rootPath;
        TOOL.namingRule = namingRule;
        if (null == sql) {
            TOOL.dbSql = DataBase.init(TOOL.url).sql();
        } else {
            TOOL.dbSql = sql;
        }
        TOOL.sqlList = new ArrayList<>();
        TOOL.manualSqlList = new ArrayList<>();
        return TOOL;
    }

    public void run() {
        run(false);
    }

    public void run(boolean update) {
        run(update, new String[]{});
    }

    public void run(Class<?>... classes) {
        run(false, classes);
    }

    public void run(String... packageName) {
        run(false, packageName);
    }

    public void run(boolean update, String... packageName) {
        List<Class<?>> classList;
        if (packageName.length > 0) {
            classList = Util.scan(rootPath, clazz -> {
                for (String pkg : packageName) {
                    if (clazz.getPackage().toString().contains(pkg)) {
                        Table table = clazz.getAnnotation(Table.class);
                        return null != table;
                    }
                }
                return false;
            });
        } else {
            classList = Util.scan(rootPath, clazz -> {
                Table table = clazz.getAnnotation(Table.class);
                return null != table;
            });
        }
        if (classList.isEmpty()) {
            throw new RuntimeException("找不到符合条件的类");
        }
        Class<?>[] classes = new Class[classList.size()];
        run(update, classList.toArray(classes));
    }

    /**
     * 执行
     *
     * @param update  是否执行修改列和删除列操作
     * @param classes 表
     */
    public void run(boolean update, Class<?>... classes) {
        if (classes.length == 0) {
            System.err.println("没有要生成SQL的类,不会进行操作");
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
            List<Class<?>> list = Arrays.asList(classes);
            create(list, conn, update);
        } catch (Exception e) {
            printSql();
            throw new RuntimeException(e);
        }
        printSql();
    }

    private void create(List<Class<?>> classes, Connection conn, boolean update) {
        for (Class<?> clazz : classes) {
            TableInfo tableInfo = Sql.getInfo(clazz, namingRule);
            try {
                if (dbSql.checkTableExists(conn, tableInfo.getName())) {
                    System.out.println("数据表: " + tableInfo.getName() + " 已经存在,即将检查对比,尝试进行修改 ... ");
                    try {
                        update(tableInfo, conn, update);
                    } catch (SQLException e1) {
                        System.out.print("修改实体: " + clazz.getSimpleName() + " 的数据表 失败!!!!!!");
                        throw new RuntimeException(e1);
                    }
                } else {
                    System.out.println("创建实体: " + clazz.getSimpleName() + " 的数据表 ... 开始");
                    executeSql(conn, Sql.BR + dbSql.getCreate(tableInfo));
                    System.out.println("创建实体: " + clazz.getSimpleName() + " 的数据表 ... 成功!!!");
                }
            } catch (SQLException e) {
                System.out.println("失败");
                e.printStackTrace();
            }
        }
    }

    private void update(TableInfo tableInfo, Connection conn, boolean update) throws SQLException {
        if (!tableInfo.getComment().equals(dbSql.getTableComment(conn, tableInfo.getName()))) {
            System.out.println("修改表的注释,表: " + tableInfo.getName());
            executeSql(conn, dbSql.getAlterTableComment(tableInfo));
        }
        List<ColumnInfo> javaList = tableInfo.getColumnInfoList();
        List<ColumnInfo> dbList = Sql.getInfo(conn, tableInfo.getName());
        ColumnCompareInfo compare = Sql.compare(javaList, dbList);
        if (compare.isEmpty()) {
            System.out.println("数据表: " + tableInfo.getName() + " 没有变动,直接跳过 ...");
            return;
        }
        System.out.println(Sql.TAB + "修改表: " + tableInfo.getName() + " ... 开始");
        if (!compare.getAddList().isEmpty()) {
            System.out.println(Sql.TAB + Sql.TAB + "需要增加列:" + compare.getAddList() + " 开始执行==>");
            for (ColumnInfo columnInfo : compare.getAddList()) {
                System.out.print(Sql.TAB + Sql.TAB + "增加列: " + columnInfo.getName() + " ... ");
                executeSql(conn, dbSql.getAlterAddColumn(tableInfo.getName(), columnInfo));
                System.out.println("成功");
            }
        }
        if (!compare.getDelList().isEmpty()) {
            System.out.println(Sql.TAB + Sql.TAB + "需要删除列:" + compare.getDelList() + " 开始执行==>");
            for (ColumnInfo columnInfo : compare.getDelList()) {
                String sql = dbSql.getDropColumn(tableInfo.getName(), columnInfo.getName());
                if (update) {
                    System.out.print(Sql.TAB + Sql.TAB + "删除列: " + columnInfo.getName() + " ... ");
                    executeSql(conn, sql);
                    System.out.println("成功");
                    System.out.println();
                } else {
                    manualSql(sql);
                }
            }
        }
        if (!compare.getModifyList().isEmpty()) {
            System.out.println(Sql.TAB + Sql.TAB + "需要修改列:" + compare.getModifyList() + " 开始执行==>");
            for (ColumnInfo columnInfo : compare.getModifyList()) {
                String sql = dbSql.getAlterModifyColumn(tableInfo.getName(), columnInfo);
                if (update) {
                    System.out.print(Sql.TAB + Sql.TAB + "修改列: " + columnInfo.getName() + " ... ");
                    executeSql(conn, sql);
                    System.out.println("成功");
                    System.out.println();
                } else {
                    manualSql(sql);
                }

            }
        }
        System.out.println(Sql.TAB + "修改表: " + tableInfo.getName() + " ... 结束");
    }

    private void executeSql(Connection conn, String sql) throws SQLException {
        sqlList.add(sql + ";");
        try {
            conn.prepareStatement(sql).execute();
        } catch (SQLException e) {
            sqlList.add("#执行失败!!!!!!!!!!!!!!!!!!!" + Sql.BR);
            throw e;
        }
        sqlList.add("#执行成功==================>" + Sql.BR);
    }

    private void manualSql(String sql) {
        System.out.println(Sql.TAB + Sql.TAB + "请手动执行:");
        System.out.println(Sql.TAB + Sql.TAB + sql);
        manualSqlList.add(sql + ";");
    }

    private void printSql() {
        if (!sqlList.isEmpty()) {
            System.out.println(Sql.BR + "#=====所有已经被执行的SQL如下=====>:" + Sql.BR);
            for (String sql : sqlList) {
                System.out.println(sql);
            }
        }
        if (!manualSqlList.isEmpty()) {
            System.out.println();
            System.out.println(Sql.BR + "#=====需要您手动执行的SQL如下=====>:" + Sql.BR);
            for (String sql : manualSqlList) {
                System.out.println(sql);
            }
        }
    }


    private SqlBuilder() {
    }
}
