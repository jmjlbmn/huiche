package org.huiche.extra.sql.builder.info;

import java.io.Serializable;
import java.sql.JDBCType;

/**
 * 列信息对象
 *
 * @author Maning
 */
public class ColumnInfo implements Serializable {
    private String name;
    private JDBCType type;
    private Integer length;
    private Integer precision;
    private Boolean unique;
    private Boolean notNull;
    private Boolean isPrimaryKey;
    private Boolean isAutoIncrement;
    private String comment;

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ColumnInfo && name.equalsIgnoreCase(((ColumnInfo) obj).name);
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public ColumnInfo setName(String name) {
        this.name = name;
        return this;
    }

    public JDBCType getType() {
        return type;
    }

    public ColumnInfo setType(JDBCType type) {
        this.type = type;
        return this;
    }

    public Integer getLength() {
        return length;
    }

    public ColumnInfo setLength(Integer length) {
        this.length = length;
        return this;
    }

    public Integer getPrecision() {
        return precision;
    }

    public ColumnInfo setPrecision(Integer precision) {
        this.precision = precision;
        return this;
    }

    public Boolean getUnique() {
        return unique;
    }

    public ColumnInfo setUnique(Boolean unique) {
        this.unique = unique;
        return this;
    }

    public Boolean getNotNull() {
        return notNull;
    }

    public ColumnInfo setNotNull(Boolean notNull) {
        this.notNull = notNull;
        return this;
    }

    public Boolean getPrimaryKey() {
        return isPrimaryKey;
    }

    public ColumnInfo setPrimaryKey(Boolean primaryKey) {
        isPrimaryKey = primaryKey;
        return this;
    }

    public Boolean getAutoIncrement() {
        return isAutoIncrement;
    }

    public ColumnInfo setAutoIncrement(Boolean autoIncrement) {
        isAutoIncrement = autoIncrement;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public ColumnInfo setComment(String comment) {
        this.comment = comment;
        return this;
    }

}
