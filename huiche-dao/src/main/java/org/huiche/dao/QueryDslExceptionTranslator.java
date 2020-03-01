package org.huiche.dao;

import com.querydsl.sql.SQLExceptionTranslator;

import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.support.SQLStateSQLExceptionTranslator;

/**
 * @author Maning
 */
public class QueryDslExceptionTranslator implements SQLExceptionTranslator {
    private final org.springframework.jdbc.support.SQLExceptionTranslator translator;

    public QueryDslExceptionTranslator() {
        this.translator = new SQLStateSQLExceptionTranslator();
    }

    public QueryDslExceptionTranslator(org.springframework.jdbc.support.SQLExceptionTranslator translator) {
        this.translator = translator;
    }

    @Override
    public RuntimeException translate(String sql, List<Object> bindings, SQLException e) {
        return this.translator.translate(null, sql, e);
    }

    @Override
    public RuntimeException translate(SQLException e) {
        return this.translator.translate(null, null, e);
    }
}
