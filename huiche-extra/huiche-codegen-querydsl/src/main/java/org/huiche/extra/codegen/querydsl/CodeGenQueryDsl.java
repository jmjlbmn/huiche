package org.huiche.extra.codegen.querydsl;

import com.querydsl.codegen.BeanSerializer;
import com.querydsl.sql.codegen.MetaDataExporter;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Maning
 */
public class CodeGenQueryDsl {
    private String jdbcUrl;
    private String user;
    private String password;
    private String exporterPath;

    private CodeGenQueryDsl() {
    }

    public static CodeGenQueryDsl init(String jdbcUrl, String user, String password, String exporterPath) {
        initDriver(jdbcUrl);
        CodeGenQueryDsl codeGenQueryDsl = new CodeGenQueryDsl();
        codeGenQueryDsl.jdbcUrl = jdbcUrl;
        codeGenQueryDsl.user = user;
        codeGenQueryDsl.password = password;
        codeGenQueryDsl.exporterPath = exporterPath;
        return codeGenQueryDsl;
    }

    public void exportTable() {
        exportTable(null);
    }

    public void exportTable(String packageName) {
        try (Connection conn = DriverManager.getConnection(jdbcUrl, user, password)) {
            MetaDataExporter exporter = new MetaDataExporter();
            exporter.setPackageName(null == packageName ? "table" : packageName);
            exporter.setTargetFolder(new File(exporterPath));
            exporter.setBeansTargetFolder(new File(exporterPath + "/bean"));
            exporter.setExportTables(true);
            exporter.setExportViews(false);
            exporter.setBeanSerializer(new BeanSerializer());
            exporter.export(conn.getMetaData());
        } catch (SQLException e) {
            throw new RuntimeException("生成表失败!", e);
        }
    }

    public void exportView() {
        exportView(null);
    }

    public void exportView(String packageName) {
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
     * 初始化驱动
     *
     * @param url JDBC_URL
     */
    private static void initDriver(String url) {
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

    interface Sql {
        String MY_SQL = "jdbc:mysql";
    }
}
