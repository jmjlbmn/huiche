package org.huiche.extra.sql.builder.sql;

import org.huiche.extra.sql.builder.info.ColumnInfo;
import org.huiche.extra.sql.builder.info.TableInfo;

import java.util.Iterator;

/**
 * @author Maning
 * @date 2018/1/24
 */
public class Mysql implements Sql {
    @Override
    public String getCreate(TableInfo tableInfo) {
        StringBuilder builder = new StringBuilder();
        builder.append(getCreateTableStart(tableInfo));
        Iterator<ColumnInfo> iterator = tableInfo.getColumnInfoList().iterator();
        while (iterator.hasNext()) {
            ColumnInfo columnInfo = iterator.next();
            builder.append(getCreateColumn(columnInfo));
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
        builder.append(TAB).append(columnInfo.getName()).append(SPACE);
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
                if (columnInfo.getLength() < 5000) {
                    builder.append("VARCHAR");
                    builder.append(BRACKETS_START).append(columnInfo.getLength()).append(BRACKETS_END);
                } else if (columnInfo.getLength() < 65000) {
                    builder.append("TEXT");
                } else {
                    builder.append("LONGTEXT");
                }
                break;
            default:
                throw new RuntimeException("目前不支持此JDBC类型:" + columnInfo.getType().getName());
        }
        if (columnInfo.getPrimaryKey()) {
            builder.append(" PRIMARY KEY AUTO_INCREMENT");
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

    private static final Mysql SQL = new Mysql();

    public static Sql sql() {
        return SQL;
    }

    private Mysql() {
    }
}
