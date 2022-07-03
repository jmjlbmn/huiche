package org.huiche.codegen.dialect;

import org.huiche.codegen.domain.ColumnInfo;
import org.huiche.codegen.domain.TableInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Maning
 */
public interface SqlDialect {

    /**
     * 建表语句
     *
     * @param table   表格信息
     * @param columns 字段信息
     * @return ddl
     */
    @NotNull
    default String createTable(@NotNull TableInfo table, @NotNull Collection<ColumnInfo> columns) {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE ")
                .append(tableName(table))
                .append(" (\n");
        List<String> lines = new ArrayList<>();
        List<String> pks = new ArrayList<>();
        List<String> uks = new ArrayList<>();
        for (ColumnInfo column : columns) {
            lines.add(createColumn(column));
            if (column.isPrimaryKey()) {
                pks.add(column.getColumnName());
            } else if (column.isUnique()) {
                uks.add(column.getColumnName());
            }
        }
        if (!pks.isEmpty()) {
            lines.add(tablePrimaryKey(pks));
        }
        if (!uks.isEmpty()) {
            for (String uk : uks) {
                lines.add(tableUniqueKey(uk));
            }
        }
        int size = lines.size();
        for (int i = 0; i < size; i++) {
            builder.append("\t")
                    .append(lines.get(i));
            if (i < size - 1) {
                builder.append(",\n");
            }
        }
        builder.append("\n)");
        String additional = tableAdditional(table);
        if (additional != null) {
            builder.append(" ").append(additional);
        }
        builder.append(";");
        return builder.toString();
    }


    /**
     * column 定义
     *
     * @param column 列信息
     * @return ddl
     */
    @NotNull
    default String createColumn(@NotNull ColumnInfo column) {
        List<String> parts = new ArrayList<>();
        parts.add(columnName(column));
        if (column.getDefinition() != null) {
            parts.add(column.getDefinition());
        } else {
            parts.add(columnDefinition(column));
        }
        if (column.getDefaultValue() != null) {
            parts.add(columnDefaultValue(column));
        }
        if (column.isAutoIncrement()) {
            parts.add(columnAutoIncrement(column));
        }
        if (!column.isNullable()) {
            parts.add("NOT NULL");
        }
        String additional = columnAdditional(column);
        if (additional != null) {
            parts.add(additional);
        }
        return String.join(" ", parts);
    }

    /**
     * 引用字符,用于表名,列名
     *
     * @return 引用字符
     */
    @NotNull
    default String quoteStr() {
        return "";
    }

    /**
     * 转义标识 用于字符串
     *
     * @return 转义标识
     */
    @NotNull
    default String escapeStr() {
        return "\"";
    }

    /**
     * 列定义
     *
     * @param column 字段信息
     * @return ddl
     */
    @NotNull
    default String columnDefinition(@NotNull ColumnInfo column) {
        return column.getJdbcType().getName() + columnLengthAndSignStatus(column);
    }

    /**
     * 列长度和sign状态定义
     *
     * @param column 列信息
     * @return ddl
     */
    @NotNull
    default String columnLengthAndSignStatus(@NotNull ColumnInfo column) {
        String definition = "";
        if (column.getLength() != null) {
            if (column.getPrecision() != null) {
                definition += "(" + column.getLength() + "," + column.getPrecision() + ")";
            } else {
                definition += "(" + column.getLength() + ")";
            }
        }
        if (column.isUnsigned()) {
            definition += " UNSIGNED";
        }
        return definition;
    }

    /**
     * 默认值定义
     *
     * @param column 列信息
     * @return ddl
     */
    @NotNull
    default String columnDefaultValue(@NotNull ColumnInfo column) {
        return "DEFAULT " + column.getDefaultValue();
    }

    /**
     * 字段备注定义
     *
     * @param column 列信息
     * @return ddl
     */
    @NotNull
    default String columnComment(@NotNull ColumnInfo column) {
        return "COMMENT " + escapeStr() + column.getComment() + escapeStr();
    }

    /**
     * 列名定义
     *
     * @param column 列信息
     * @return ddl
     */
    @NotNull
    default String columnName(@NotNull ColumnInfo column) {
        return columnName(column.getColumnName());
    }

    /**
     * 列名定义
     *
     * @param columnName 列名
     * @return ddl
     */
    @NotNull
    default String columnName(@NotNull String columnName) {
        return quoteStr() + columnName + quoteStr();
    }

    /**
     * 列自增定义
     *
     * @param column 列信息
     * @return ddl
     */
    @NotNull
    default String columnAutoIncrement(@NotNull ColumnInfo column) {
        return "GENERATED ALWAYS AS IDENTITY";
    }

    /**
     * 列附加信息
     *
     * @param column 列信息
     * @return ddl
     */
    @Nullable
    default String columnAdditional(@NotNull ColumnInfo column) {
        List<String> list = new ArrayList<>(2);
        if (column.getAdditional() != null) {
            list.add(column.getAdditional());
        }
        if (column.getComment() != null) {
            list.add(columnComment(column));
        }
        if (list.isEmpty()) {
            return null;
        } else {
            return String.join(" ", list);
        }
    }

    /**
     * 主键定义
     *
     * @param pks 主键
     * @return ddl
     */
    @NotNull
    default String tablePrimaryKey(@NotNull List<String> pks) {
        return "CONSTRAINT pk PRIMARY KEY (" + pks.stream().map(this::columnName).collect(Collectors.joining(",")) + ")";
    }

    /**
     * 唯一索引定义
     *
     * @param uk 列
     * @return ddl
     */
    @NotNull
    default String tableUniqueKey(@NotNull String uk) {
        return "CONSTRAINT uk_" + uk + " PRIMARY KEY (" + columnName(uk) + ")";
    }

    /**
     * 表名定义
     *
     * @param table 表信息
     * @return ddl
     */
    @NotNull
    default String tableName(@NotNull TableInfo table) {
        String name = quoteStr() + table.getTableName() + quoteStr();
        return table.getSchema() != null ? table.getSchema() + "." + name : name;
    }

    /**
     * 表备注定义
     *
     * @param table 表信息
     * @return ddl
     */
    @NotNull
    default String tableComment(@NotNull TableInfo table) {
        return "COMMENT = " + escapeStr() + table.getComment() + escapeStr();
    }

    /**
     * 表附加信息定义
     *
     * @param table 表信息
     * @return ddl
     */
    @Nullable
    default String tableAdditional(@NotNull TableInfo table) {
        List<String> list = new ArrayList<>(2);
        if (table.getAdditional() != null) {
            list.add(table.getAdditional());
        }
        if (table.getComment() != null) {
            list.add(tableComment(table));
        }
        if (list.isEmpty()) {
            return null;
        } else {
            return String.join(" ", list);
        }
    }
}
