package org.huiche.dao;

import com.querydsl.sql.SQLQueryFactory;
import org.huiche.data.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Maning
 */
public class BaseDao implements Query {
    @Autowired(required = false)
    private SQLQueryFactory sqlQueryFactory;

    protected SQLQueryFactory sql() {
        return sqlQueryFactory;
    }
}

