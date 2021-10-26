package org.huiche.config;

import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.inject.Provider;
import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @author Maning
 */
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