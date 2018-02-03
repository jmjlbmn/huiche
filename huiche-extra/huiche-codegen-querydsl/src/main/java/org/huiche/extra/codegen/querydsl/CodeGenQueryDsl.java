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
    public static void run(String jdbcUrl, String user, String password, String packageName, String exporterPath) {
        initDriver(jdbcUrl);
        try (Connection conn = DriverManager.getConnection(jdbcUrl, user, password)) {
            export(conn, packageName, exporterPath);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void export(Connection conn, String packageName, String exporterPath) throws SQLException {
        MetaDataExporter exporter = new MetaDataExporter();
        exporter.setPackageName(packageName);
        exporter.setTargetFolder(new File(exporterPath));
        exporter.setExportTables(true);
        exporter.setExportViews(true);
        exporter.setBeanSerializer(new BeanSerializer());
        exporter.export(conn.getMetaData());
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
