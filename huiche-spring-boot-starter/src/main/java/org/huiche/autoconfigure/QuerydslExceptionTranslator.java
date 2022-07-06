package org.huiche.autoconfigure;

import com.querydsl.sql.SQLExceptionTranslator;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Maning
 */
public class QuerydslExceptionTranslator implements SQLExceptionTranslator {
    private final org.springframework.jdbc.support.SQLExceptionTranslator translator;

    public QuerydslExceptionTranslator() {
        this.translator = new SQLErrorCodeSQLExceptionTranslator();
    }

    @Override
    public RuntimeException translate(String sql, List<Object> bindings, SQLException e) {
        DataAccessException exception = this.translator.translate("querydsl", sql, e);
        if (exception != null) {
            return exception;
        } else {
            return new RuntimeException(e);
        }
    }

    @Override
    public RuntimeException translate(SQLException e) {
        return this.translator.translate("querydsl", null, e);
    }
}
