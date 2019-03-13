package org.huiche.extra.sql.builder.sql;

import org.huiche.extra.sql.builder.info.ColumnInfo;
import org.huiche.extra.sql.builder.info.TableInfo;

import javax.annotation.Nonnull;
import java.sql.JDBCType;
import java.util.Iterator;

/**
 * MySql建表
 *
 * @author Maning
 */
public class Mysql implements Sql {
    private static final Mysql SQL = new Mysql();

    private Mysql() {
    }

    private static boolean canAutoIncrement(@Nonnull ColumnInfo columnInfo) {
        return JDBCType.BIGINT.equals(columnInfo.getType()) || JDBCType.INTEGER.equals(columnInfo.getType());
    }

    public static Sql sql() {
        return SQL;
    }

    @Override
    @Nonnull
    public String getCreate(@Nonnull TableInfo tableInfo) {
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
    @Nonnull
    public String getCreateTableEnd(@Nonnull TableInfo tableInfo) {
        String comment = tableInfo.getComment();
        comment = null == comment || "".equals(comment.trim()) ? null : comment;
        return BR + BRACKETS_END + " ENGINE = " + engine(tableInfo.getEngine()) + " DEFAULT CHARSET = " + charset(tableInfo.getCharset()) + collation(tableInfo.getCollation()) + (null == comment ? "" : " COMMENT " + wrap(comment));
    }

    @Override
    @Nonnull
    public String getCreateColumn(@Nonnull ColumnInfo columnInfo) {
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
            case FLOAT:
                builder.append("FLOAT");
                if (null != columnInfo.getLength()) {
                    builder.append(BRACKETS_START).append(columnInfo.getLength()).append(COMMA);
                    builder.append(null == columnInfo.getPrecision() ? 0 : columnInfo.getPrecision());
                    builder.append(BRACKETS_END);
                }
                break;
            case DOUBLE:
                builder.append("DOUBLE");
                if (null != columnInfo.getLength()) {
                    builder.append(BRACKETS_START).append(columnInfo.getLength()).append(COMMA);
                    builder.append(null == columnInfo.getPrecision() ? 0 : columnInfo.getPrecision());
                    builder.append(BRACKETS_END);
                }
                break;
            case DECIMAL:
                builder.append("DECIMAL");
                if (null != columnInfo.getLength()) {
                    builder.append(BRACKETS_START).append(columnInfo.getLength()).append(COMMA);
                    builder.append(null == columnInfo.getPrecision() ? 0 : columnInfo.getPrecision());
                    builder.append(BRACKETS_END);
                }
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
        if (columnInfo.getIsPrimaryKey()) {
            if (canAutoIncrement(columnInfo)) {
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

    interface Length {
        int TEXT = 4000;
        int LONGTEXT = 60000;
    }

    private String engine(String engine) {
        if ("".equals(engine)) {
            return "InnoDB";
        } else {
            return engine;
        }
    }

    private String charset(String charset) {
        if ("".equals(charset)) {
            return "utf8mb4";
        } else {
            return charset;
        }
    }

    private String collation(String collation) {
        if ("".equals(collation)) {
            return "";
        } else {
            return " COLLATE = " + collation;
        }
    }
}
