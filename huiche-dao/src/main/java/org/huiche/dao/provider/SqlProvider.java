package org.huiche.dao.provider;

import com.querydsl.sql.SQLQueryFactory;

/**
 * @author Maning
 */
public interface SqlProvider {
    SQLQueryFactory sql();
}
