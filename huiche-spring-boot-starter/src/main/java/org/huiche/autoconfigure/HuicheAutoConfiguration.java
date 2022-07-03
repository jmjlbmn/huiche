package org.huiche.autoconfigure;

import com.querydsl.sql.*;
import org.huiche.dao.EnumTypePool;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * @author Maning
 */
@AutoConfiguration(after = DataSourceAutoConfiguration.class)
@Import(QuerydslMapperRegistrar.class)
public class HuicheAutoConfiguration {
    private static final String PARENT_CONTEXT = AbstractSQLQuery.class.getName() + "#PARENT_CONTEXT";

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnSingleCandidate(DataSource.class)
    public SQLTemplates sqlTemplates(DataSource dataSource) throws SQLException {
        Connection connection = dataSource.getConnection();
        DatabaseMetaData metaData = connection.getMetaData();
        String type = metaData.getDatabaseProductName().toLowerCase();
        String version = metaData.getDatabaseProductVersion().toLowerCase();
        DataSourceUtils.releaseConnection(connection, dataSource);
        if (type.contains("mysql") || type.contains("mariadb")) {
            return MySQLTemplates.DEFAULT;
        }
        if (type.contains("oracle")) {
            return OracleTemplates.DEFAULT;
        }
        if (type.contains("microsoft sql server") || type.contains("azure")) {
            if (version.startsWith("9")) {
                return SQLServer2005Templates.DEFAULT;
            }
            if (version.startsWith("10")) {
                return SQLServer2008Templates.DEFAULT;
            }
            return SQLServer2012Templates.DEFAULT;
        }
        if (type.contains("postgresql")) {
            return PostgreSQLTemplates.DEFAULT;
        }
        if (type.contains("db2") || type.contains("z/os") || type.contains("sqlds") || type.contains("iseries") || type.contains("cloudscape") || type.contains("informix")) {
            return DB2Templates.DEFAULT;
        }
        if (type.contains("sqlite")) {
            return SQLiteTemplates.DEFAULT;
        }
        if (type.contains("derby")) {
            return DerbyTemplates.DEFAULT;
        }
        if (type.contains("h2")) {
            return H2Templates.DEFAULT;
        }
        if (type.contains("hsql")) {
            return HSQLDBTemplates.DEFAULT;
        }
        if (type.contains("teradata")) {
            return TeradataTemplates.DEFAULT;
        }
        if (type.contains("firebird")) {
            return FirebirdTemplates.DEFAULT;
        }
        if (type.contains("cubird")) {
            return CUBRIDTemplates.DEFAULT;
        }
        return SQLTemplates.DEFAULT;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnSingleCandidate(DataSource.class)
    public Configuration configuration(SQLTemplates sqlTemplates) {
        Configuration configuration = new Configuration(sqlTemplates);
        configuration.setExceptionTranslator(new QuerydslExceptionTranslator());
        EnumTypePool.types().forEach(configuration::register);
        return configuration;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnSingleCandidate(DataSource.class)
    public SQLQueryFactory sqlQueryFactory(DataSource dataSource, Configuration configuration) {
        configuration.addListener(new SQLBaseListener() {
            @Override
            public void end(SQLListenerContext context) {
                Connection connection = context.getConnection();
                if (connection != null && context.getData(PARENT_CONTEXT) == null) {
                    DataSourceUtils.releaseConnection(connection, dataSource);
                }
            }
        });
        return new SQLQueryFactory(configuration, () -> DataSourceUtils.getConnection(dataSource));
    }
}
