package org.huiche.extra.sql.builder.info;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.huiche.annotation.sql.Column;

import javax.annotation.Nonnull;

/**
 * 字段/列信息对象
 *
 * @author Maning
 */
@Setter
@Getter
@Accessors(chain = true)
@ToString
public class FieldColumn {
    private String name;
    private Class<?> type;
    private Column column;

    public FieldColumn(@Nonnull String name, @Nonnull Class<?> type, @Nonnull Column column) {
        this.name = name;
        this.type = type;
        this.column = column;
    }
}
