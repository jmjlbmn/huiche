package org.huiche.codegen.domain;

import org.huiche.annotation.Table;
import org.huiche.support.NamingUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Maning
 */
public class TableInfo {
    @NotNull
    private final String tableName;
    @Nullable
    private final String comment;
    @Nullable
    private final String schema;
    @Nullable
    private final String additional;

    private TableInfo(@NotNull Table table, @NotNull String className) {
        if (!"".equals(table.name())) {
            this.tableName = table.name();
        } else {
            this.tableName = NamingUtil.camel2underLine(className);
        }
        if (!"".equals(table.schema())) {
            this.schema = table.schema();
        } else {
            this.schema = null;
        }
        this.comment = !"".equals(table.comment()) ? table.comment() : null;
        this.additional = !"".equals(table.additional()) ? table.additional() : null;
    }

    @NotNull
    public static TableInfo of(@NotNull Class<?> clazz) {
        Table table = clazz.getAnnotation(Table.class);
        if (table == null) {
            throw new RuntimeException("必须标注@Table注解");
        }
        return new TableInfo(table, clazz.getSimpleName());
    }

    @NotNull
    public String getTableName() {
        return tableName;
    }

    @Nullable
    public String getComment() {
        return comment;
    }

    @Nullable
    public String getSchema() {
        return schema;
    }

    @Nullable
    public String getAdditional() {
        return additional;
    }
}
