package org.huiche.extra.sql.builder.info;

import org.huiche.core.annotation.Column;

/**
 * @author Maning
 */
public class FieldColumn {
    private String name;
    private Class<?> type;
    private Column column;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }

    public FieldColumn(String name, Class<?> type, Column column) {
        this.name = name;
        this.type = type;
        this.column = column;
    }

    public FieldColumn() {
    }

    @Override
    public String toString() {
        return "FieldColumn{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", column=" + column +
                '}';
    }
}
