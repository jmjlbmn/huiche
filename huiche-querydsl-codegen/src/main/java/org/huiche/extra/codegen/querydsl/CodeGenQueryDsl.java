package org.huiche.extra.codegen.querydsl;

import com.querydsl.codegen.BeanSerializer;
import com.querydsl.sql.codegen.MetaDataExporter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * QueryDsl查询实体生成
 *
 * @author Maning
 */
public class CodeGenQueryDsl {
    private String jdbcUrl;
    private String exporterPath;
    private Properties props;

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
        CodeGenQueryDsl codeGenQueryDsl = new CodeGenQueryDsl();
        codeGenQueryDsl.jdbcUrl = jdbcUrl;
        codeGenQueryDsl.exporterPath = exporterPath;
        codeGenQueryDsl.props = new Properties();
        codeGenQueryDsl.props.setProperty("user", user);
        codeGenQueryDsl.props.setProperty("password", password);
        codeGenQueryDsl.props.setProperty("nullCatalogMeansCurrent", "true");
        return codeGenQueryDsl;
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
        try (Connection conn = DriverManager.getConnection(jdbcUrl, props)) {
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
        try (Connection conn = DriverManager.getConnection(jdbcUrl, props)) {
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
}
