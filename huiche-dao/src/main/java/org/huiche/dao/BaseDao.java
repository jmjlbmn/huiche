package org.huiche.dao;

import com.querydsl.sql.SQLQueryFactory;
import org.huiche.dao.provider.SqlProvider;
import org.huiche.data.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Maning
 */
public class BaseDao implements Query, SqlProvider {
    @Autowired(required = false)
    private SQLQueryFactory sqlQueryFactory;

    @Override
    public SQLQueryFactory sql() {
        return sqlQueryFactory;
    }
}

