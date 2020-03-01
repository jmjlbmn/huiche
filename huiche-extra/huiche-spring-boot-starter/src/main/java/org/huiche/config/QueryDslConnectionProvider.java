package org.huiche.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.inject.Provider;
import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @author Maning
 */
@Slf4j
public class QueryDslConnectionProvider implements Provider<Connection> {
    private final DataSource dataSource;

    public QueryDslConnectionProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection get() {
        return DataSourceUtils.getConnection(this.dataSource);
    }
}