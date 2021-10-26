package org.huiche.extra.sql.builder.info;

import org.huiche.annotation.sql.Column;

import javax.annotation.Nonnull;

/**
 * 字段/列信息对象
 *
 * @author Maning
 */
public class FieldColumn {
    private String name;
    private Class<?> type;
    private Column column;

    public FieldColumn(@Nonnull String name, @Nonnull Class<?> type, @Nonnull Column column) {
        this.name = name;
        this.type = type;
        this.column = column;
    }

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
}
