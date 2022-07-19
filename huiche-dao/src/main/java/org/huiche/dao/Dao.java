package org.huiche.dao;

import com.querydsl.sql.SQLQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Maning
 */
public class Dao {
    @Autowired
    protected SQLQueryFactory sql;

    public SQLQueryFactory sql() {
        return sql;
    }
}
