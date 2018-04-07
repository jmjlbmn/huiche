package org.huiche.config;

import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.spring.SpringConnectionProvider;
import org.huiche.core.dao.MySqlExTemplates;
import org.huiche.core.dao.QueryDsl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nullable;
import javax.inject.Provider;
import javax.sql.DataSource;
import java.sql.Connection;

/**
 * QueryDsl 自动配置
 *
 * @author Maning
 */
@Configuration
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class QueryDslAutoConfigure {
    /**
     * 注册SQLQueryFactory,默认MySql,如用其他db请注册SQLTemplates的bean 或自行注册SQLQueryFactory
     *
     * @param dataSource   数据源
     * @param sqlTemplates SQL模板
     * @return SQLQueryFactory
     */
    @Bean
    @ConditionalOnBean(DataSource.class)
    @ConditionalOnMissingBean
    public SQLQueryFactory sqlQueryFactory(DataSource dataSource, @Nullable @Autowired(required = false) SQLTemplates sqlTemplates) {
        Provider<Connection> provider = new SpringConnectionProvider(dataSource);
        if (null == sqlTemplates) {
            sqlTemplates = new MySqlExTemplates();
        }
        QueryDsl.init(sqlTemplates);
        return new SQLQueryFactory(QueryDsl.CONFIG, provider);
    }
}
