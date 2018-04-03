package org.huiche.extra.sql.builder.info;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.JDBCType;

/**
 * 列信息对象
 *
 * @author Maning
 */
@Setter
@Getter
@Accessors(chain = true)
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
}
