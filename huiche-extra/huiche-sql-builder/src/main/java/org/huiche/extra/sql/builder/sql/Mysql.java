package org.huiche.extra.sql.builder.sql;

import org.huiche.extra.sql.builder.info.ColumnInfo;
import org.huiche.extra.sql.builder.info.TableInfo;

import java.sql.JDBCType;
import java.util.Iterator;

/**
 * @author Maning
 */
public class Mysql implements Sql {
    @Override
    public String getCreate(TableInfo tableInfo) {
        StringBuilder builder = new StringBuilder();
        builder.append(getCreateTableStart(tableInfo));
        Iterator<ColumnInfo> iterator = tableInfo.getColumnInfoList().iterator();
        while (iterator.hasNext()) {
            ColumnInfo columnInfo = iterator.next();
            builder.append(TAB).append(getCreateColumn(columnInfo));
            if (iterator.hasNext()) {
                builder.append(COMMA);
                builder.append(BR);
            }
        }

        builder.append(getCreateTableEnd(tableInfo));
        return builder.toString();
    }

    @Override
    public String getCreateTableEnd(TableInfo tableInfo) {
        String comment = tableInfo.getComment();
        comment = null == comment || "".equals(comment.trim()) ? null : comment;
        return BR + BRACKETS_END + " ENGINE = " + tableInfo.getEngine() + " DEFAULT CHARSET = " + tableInfo.getCharset() + (null == comment ? "" : " COMMENT " + wrap(comment));
    }

    @Override
    public String getCreateColumn(ColumnInfo columnInfo) {
        StringBuilder builder = new StringBuilder();
        builder.append(columnInfo.getName()).append(SPACE);
        switch (columnInfo.getType()) {
            case TINYINT:
                builder.append("TINYINT");
                if (null != columnInfo.getLength()) {
                    builder.append(BRACKETS_START).append(columnInfo.getLength()).append(BRACKETS_END);
                }
                break;
            case INTEGER:
                builder.append("INT");
                break;
            case BIGINT:
                builder.append("BIGINT");
                break;
            case DECIMAL:
                builder.append("DECIMAL");
                builder.append(BRACKETS_START).append(columnInfo.getLength()).append(COMMA).append(columnInfo.getPrecision()).append(BRACKETS_END);
                break;
            case VARCHAR:
                if (columnInfo.getLength() >= Length.LONGTEXT) {
                    builder.append("LONGTEXT");
                } else if (columnInfo.getLength() >= Length.TEXT) {
                    builder.append("TEXT");
                } else {
                    builder.append("VARCHAR");
                    builder.append(BRACKETS_START).append(columnInfo.getLength()).append(BRACKETS_END);
                }
                break;
            default:
                throw new RuntimeException("目前不支持此JDBC类型:" + columnInfo.getType().getName());
        }
        if (columnInfo.getPrimaryKey()) {
            if (columnInfo.getAutoIncrement() && canAutoIncrement(columnInfo)) {
                builder.append(" PRIMARY KEY AUTO_INCREMENT");
            } else {
                builder.append(" PRIMARY KEY");
            }
        } else {
            if (columnInfo.getNotNull()) {
                builder.append(" NOT NULL");
            }
            if (columnInfo.getUnique()) {
                builder.append(" UNIQUE");
            }
        }
        String comment = columnInfo.getComment();
        if (null != comment && !"".contentEquals(comment.trim())) {
            builder.append(" COMMENT ").append(wrap(comment));
        }
        return builder.toString();
    }

    private static boolean canAutoIncrement(ColumnInfo columnInfo) {
        return JDBCType.BIGINT.equals(columnInfo.getType()) || JDBCType.INTEGER.equals(columnInfo.getType());
    }

    private static final Mysql SQL = new Mysql();

    public static Sql sql() {
        return SQL;
    }

    private Mysql() {
    }

    interface Length {
        int TEXT = 4000;
        int LONGTEXT = 60000;
    }
}
