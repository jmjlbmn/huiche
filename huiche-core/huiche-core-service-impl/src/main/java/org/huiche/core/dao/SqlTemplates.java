package org.huiche.core.dao;

import com.querydsl.sql.MySQLTemplates;

import java.sql.Types;

/**
 * @author Maning
 */
public class SqlTemplates extends MySQLTemplates {

    @Override
    public String getCastTypeNameForCode(int code) {
        switch (code) {
            case Types.NUMERIC:
                return "decimal";
            default:
                return super.getCastTypeNameForCode(code);
        }

    }
}
