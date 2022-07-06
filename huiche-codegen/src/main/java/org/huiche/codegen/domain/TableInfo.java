package org.huiche.codegen.domain;

import org.huiche.annotation.Table;
import org.huiche.support.NamingUtil;

/**
 * @author Maning
 */
public class TableInfo {

    private final String tableName;

    private final String comment;

    private final String schema;

    private final String additional;

    private TableInfo(Table table, String className) {
        if (!"".equals(table.name())) {
            this.tableName = table.name();
        } else {
            this.tableName = NamingUtil.camel2snake(className);
        }
        if (!"".equals(table.schema())) {
            this.schema = table.schema();
        } else {
            this.schema = null;
        }
        this.comment = !"".equals(table.comment()) ? table.comment() : null;
        this.additional = !"".equals(table.additional()) ? table.additional() : null;
    }


    public static TableInfo of(Class<?> clazz) {
        Table table = clazz.getAnnotation(Table.class);
        if (table == null) {
            throw new RuntimeException("必须标注@Table注解");
        }
        return new TableInfo(table, clazz.getSimpleName());
    }


    public String getTableName() {
        return tableName;
    }


    public String getComment() {
        return comment;
    }


    public String getSchema() {
        return schema;
    }


    public String getAdditional() {
        return additional;
    }
}
