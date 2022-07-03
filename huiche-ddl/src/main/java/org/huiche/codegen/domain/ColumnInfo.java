package org.huiche.codegen.domain;

import org.huiche.annotation.Column;
import org.huiche.support.NamingUtil;
import org.huiche.support.PrimaryKey;
import org.huiche.support.TypeMapping;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.sql.JDBCType;
import java.util.Arrays;

/**
 * @author Maning
 */
public class ColumnInfo {
    @NotNull
    private final String columnName;
    @Nullable
    private final String comment;
    private final boolean nullable;
    @Nullable
    private final Integer precision;
    @NotNull
    private final JDBCType jdbcType;
    @Nullable
    private final String defaultValue;
    private final boolean primaryKey;
    private final boolean autoIncrement;
    private final boolean unsigned;
    @Nullable
    private final String definition;
    @Nullable
    private final String additional;
    private final boolean unique;
    @Nullable
    private Integer length;

    public ColumnInfo(@Nullable Column column, @NotNull String fieldName, @NotNull JDBCType defaultJdbcType) {
        if (column != null) {
            this.columnName = !"".equals(column.name()) ? column.name() : NamingUtil.camel2underLine(fieldName);
            this.comment = !"".equals(column.comment()) ? column.comment() : null;
            this.defaultValue = !"".equals(column.defaultValue()) ? column.defaultValue() : null;
            this.definition = !"".equals(column.definition()) ? column.definition() : null;
            this.additional = !"".equals(column.additional()) ? column.additional() : null;
            this.precision = column.precision() < 0 ? null : column.precision();
            this.jdbcType = JDBCType.NULL.equals(column.jdbcType()) ? defaultJdbcType : column.jdbcType();
            this.primaryKey = !PrimaryKey.NOT_PK.equals(column.primaryKey());
            this.nullable = !this.primaryKey && column.nullable();
            this.autoIncrement = PrimaryKey.AUTO.equals(column.primaryKey());
            this.unsigned = column.unsigned();
            this.unique = column.unique();
            if (column.length() >= 0) {
                this.length = column.length();
            }
        } else {
            this.columnName = NamingUtil.camel2underLine(fieldName);
            this.comment = null;
            this.defaultValue = null;
            this.definition = null;
            this.additional = null;
            this.precision = null;
            this.jdbcType = defaultJdbcType;
            this.primaryKey = false;
            this.nullable = true;
            this.autoIncrement = false;
            this.unsigned = false;
            this.unique = false;
        }
        if (this.length == null && Arrays.asList(JDBCType.VARCHAR, JDBCType.NVARCHAR, JDBCType.VARBINARY).contains(this.jdbcType)) {
            this.length = 255;
        }
    }

    @NotNull
    public static ColumnInfo of(@NotNull Field field) {
        return new ColumnInfo(field.getAnnotation(Column.class), field.getName(), TypeMapping.parseJdbcType(field.getType()));
    }

    @NotNull
    public String getColumnName() {
        return columnName;
    }

    @Nullable
    public String getComment() {
        return comment;
    }

    public boolean isNullable() {
        return nullable;
    }

    @Nullable
    public Integer getLength() {
        return length;
    }

    public void setLength(@Nullable Integer length) {
        this.length = length;
    }

    @Nullable
    public Integer getPrecision() {
        return precision;
    }

    @NotNull
    public JDBCType getJdbcType() {
        return jdbcType;
    }

    @Nullable
    public String getDefaultValue() {
        return defaultValue;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public boolean isUnsigned() {
        return unsigned;
    }

    @Nullable
    public String getDefinition() {
        return definition;
    }

    @Nullable
    public String getAdditional() {
        return additional;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public boolean isUnique() {
        return unique;
    }
}
