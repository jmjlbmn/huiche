package org.huiche.config;

import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.spring.SpringConnectionProvider;
import org.huiche.core.dao.QueryDsl;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Provider;
import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @author Maning
 */
@Configuration
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class QueryDslAutoConfigure {
    @Bean
    @ConditionalOnBean(DataSource.class)
    @ConditionalOnMissingBean
    public SQLQueryFactory sqlQueryFactory(DataSource dataSource) {
        Provider<Connection> provider = new SpringConnectionProvider(dataSource);
        return new SQLQueryFactory(QueryDsl.CONFIG, provider);
    }
}
