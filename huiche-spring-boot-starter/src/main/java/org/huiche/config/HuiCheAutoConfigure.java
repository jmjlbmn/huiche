package org.huiche.config;

import com.querydsl.sql.*;
import org.huiche.dao.QueryDslExceptionTranslator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * HuiChe自动配置
 *
 * @author Maning
 */
@Configuration
@Import(DataSourceAutoConfiguration.class)
public class HuiCheAutoConfigure {

    /**
     * 注册数据库模板
     *
     * @return SQLTemplates
     */
    @Bean
    @ConditionalOnMissingBean
    public SQLTemplates sqlTemplates() {
        return new MySQLTemplates();
    }

    /**
     * 注册SQLQueryFactory
     *
     * @param dataSource   数据源
     * @param sqlTemplates 数据库模板
     * @return SQLQueryFactory
     */
    @Bean
    @ConditionalOnMissingBean
    public SQLQueryFactory sqlQueryFactory(DataSource dataSource, SQLTemplates sqlTemplates) {
        com.querydsl.sql.Configuration configuration = new com.querydsl.sql.Configuration(sqlTemplates);
        configuration.setExceptionTranslator(new QueryDslExceptionTranslator());
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

    private static final String PARENT_CONTEXT = AbstractSQLQuery.class.getName() + "#PARENT_CONTEXT";
}
