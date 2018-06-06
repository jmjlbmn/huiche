package org.huiche.extra.codegen.querydsl;

import com.querydsl.codegen.BeanSerializer;
import com.querydsl.sql.codegen.MetaDataExporter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * QueryDsl查询实体生成
 *
 * @author Maning
 */
public class CodeGenQueryDsl {
    private String jdbcUrl;
    private String user;
    private String password;
    private String exporterPath;

    private CodeGenQueryDsl() {
    }

    /**
     * 初始化
     *
     * @param jdbcUrl      数据库jdbc链接地址
     * @param user         数据库用户名
     * @param password     数据库密码
     * @param exporterPath 生成文件的导出路径
     * @return 生成器
     */
    @Nonnull
    public static CodeGenQueryDsl init(@Nonnull String jdbcUrl, @Nonnull String user, @Nonnull String password, @Nonnull String exporterPath) {
        initDriver(jdbcUrl);
        CodeGenQueryDsl codeGenQueryDsl = new CodeGenQueryDsl();
        codeGenQueryDsl.jdbcUrl = jdbcUrl;
        codeGenQueryDsl.user = user;
        codeGenQueryDsl.password = password;
        codeGenQueryDsl.exporterPath = exporterPath;
        return codeGenQueryDsl;
    }

    /**
     * 初始化驱动
     *
     * @param url JDBC_URL
     */
    private static void initDriver(@Nonnull String url) {
        if (url.startsWith(Sql.MY_SQL)) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                    throw new RuntimeException("请检查是否引入与传入url相匹配的数据库驱动jar包,url: " + url);
                }
            }
        }
    }

    /**
     * 生成表
     */
    public void exportTable() {
        exportTable(null);
    }

    /**
     * 生成表
     *
     * @param packageName 包名
     */
    public void exportTable(@Nullable String packageName) {
        try (Connection conn = DriverManager.getConnection(jdbcUrl, user, password)) {
            MetaDataExporter exporter = new MetaDataExporter();
            exporter.setPackageName(null == packageName ? "table" : packageName);
            exporter.setTargetFolder(new File(exporterPath));
            exporter.setBeansTargetFolder(new File(exporterPath + "/bean"));
            exporter.setExportTables(true);
            exporter.setExportViews(false);
            BeanSerializer beanSerializer = new BeanSerializer();
            beanSerializer.setAddToString(true);
            exporter.setBeanSerializer(beanSerializer);
            exporter.export(conn.getMetaData());
        } catch (SQLException e) {
            throw new RuntimeException("生成表失败!", e);
        }
    }

    /**
     * 生成视图
     */
    public void exportView() {
        exportView(null);
    }

    /**
     * 生成视图
     *
     * @param packageName 包名
     */
    public void exportView(@Nullable String packageName) {
        try (Connection conn = DriverManager.getConnection(jdbcUrl, user, password)) {
            MetaDataExporter exporter = new MetaDataExporter();
            exporter.setPackageName(null == packageName ? "view" : packageName);
            exporter.setTargetFolder(new File(exporterPath));
            exporter.setExportTables(false);
            exporter.setExportViews(true);
            exporter.export(conn.getMetaData());
        } catch (SQLException e) {
            throw new RuntimeException("生成视图失败!", e);
        }
    }

    /**
     * sql类型
     */
    interface Sql {
        String MY_SQL = "jdbc:mysql";
    }
}
