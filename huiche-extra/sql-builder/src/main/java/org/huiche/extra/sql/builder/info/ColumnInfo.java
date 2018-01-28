package org.huiche.extra.sql.builder.info;

import java.io.Serializable;
import java.sql.JDBCType;

/**
 * @author Maning
 * @date 2018/1/23
 */
public class ColumnInfo implements Serializable {
    private String name;
    private JDBCType type;
    private Integer length;
    private Integer precision;
    private Boolean unique;
    private Boolean notNull;
    private Boolean isPrimaryKey;
    private String comment;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JDBCType getType() {
        return type;
    }

    public void setType(JDBCType type) {
        this.type = type;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getPrecision() {
        return precision;
    }

    public void setPrecision(Integer precision) {
        this.precision = precision;
    }

    public Boolean getNotNull() {
        return notNull;
    }

    public void setNotNull(Boolean notNull) {
        this.notNull = notNull;
    }

    public Boolean getPrimaryKey() {
        return isPrimaryKey;
    }

    public void setPrimaryKey(Boolean primaryKey) {
        isPrimaryKey = primaryKey;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getUnique() {
        return unique;
    }

    public void setUnique(Boolean unique) {
        this.unique = unique;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ColumnInfo && name.equalsIgnoreCase(((ColumnInfo) obj).name);
    }

    @Override
    public String toString() {
        return name;
    }
}
