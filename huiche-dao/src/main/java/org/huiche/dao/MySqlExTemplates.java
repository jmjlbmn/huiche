package org.huiche.dao;

import com.querydsl.sql.MySQLTemplates;

import java.sql.Types;

/**
 * 扩展的Mysql模板,修复默认MySql模板 Numeric 类型转换错误
 *
 * @author Maning
 */
public class MySqlExTemplates extends MySQLTemplates {
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
