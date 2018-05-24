package org.huiche.dao.provider;

import com.querydsl.sql.SQLQueryFactory;

/**
 * @author Maning
 */
public interface SqlProvider {
    /**
     * sql工厂
     *
     * @return sql工厂
     */
    SQLQueryFactory sql();
}
