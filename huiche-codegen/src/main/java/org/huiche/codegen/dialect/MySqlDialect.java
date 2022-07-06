package org.huiche.codegen.dialect;

import org.huiche.codegen.domain.ColumnInfo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Maning
 */
public class MySqlDialect implements SqlDialect {

    public static final MySqlDialect DEFAULT = new MySqlDialect();

    @Override
    public String quoteStr() {
        return "`";
    }

    @Override
    public String escapeStr() {
        return "'";
    }

    /**
     * <a href="https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-type-conversions.html">参考MySQL定义</a>
     */
    @Override
    public String columnDefinition(ColumnInfo column) {
        String type;
        switch (column.getJdbcType()) {
            case FLOAT:
            case REAL:
            case DOUBLE:
            case NUMERIC:
                // 强制重写为 DECIMAL
                type = "DECIMAL";
                break;
            case LONGVARCHAR:
            case LONGNVARCHAR:
            case CLOB:
            case NCLOB:
            case SQLXML:
                if (column.getLength() != null && column.getLength() > 65535) {
                    type = "LONGTEXT";
                } else {
                    type = "TEXT";
                }
                column.setLength(null);
                break;
            case NCHAR:
                type = "CHAR";
                break;
            case LONGVARBINARY:
                if (column.getLength() != null && column.getLength() > 65535) {
                    type = "LONGBLOB";
                } else {
                    type = "BLOB";
                }
                column.setLength(null);
                break;
            case TIME_WITH_TIMEZONE:
                type = "TIME";
                break;
            case TIMESTAMP:
            case TIMESTAMP_WITH_TIMEZONE:
                type = "DATETIME";
                break;
            default:
                type = column.getJdbcType().name();
                break;
        }
        return type + columnLengthAndSignStatus(column);
    }

    @Override
    public String columnAutoIncrement(ColumnInfo column) {
        return "AUTO_INCREMENT";
    }

    @Override
    public String tablePrimaryKey(List<String> pks) {
        return "PRIMARY KEY (" + pks.stream().map(this::columnName).collect(Collectors.joining(",")) + ")";
    }
}
