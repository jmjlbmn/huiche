package org.huiche.codegen.domain;

import org.huiche.annotation.Column;
import org.huiche.support.NamingUtil;
import org.huiche.support.PrimaryKey;
import org.huiche.support.ReflectUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Maning
 */
public class EntityInfo {
    private String basePackage;
    private String entityPackage;
    private String entityName;
    private String camelCaseName;
    private String idType;

    public static EntityInfo of(Class<?> clazz) {
        EntityInfo info = new EntityInfo();
        info.entityName = clazz.getSimpleName();
        info.camelCaseName = NamingUtil.pascal2camel(info.entityName);
        info.entityPackage = clazz.getPackage().getName();
        info.basePackage = info.entityPackage.substring(0, info.entityPackage.lastIndexOf("."));
        List<Field> ids = new ArrayList<>();
        for (Field field : ReflectUtil.scanNormalFields(clazz)) {
            Column column = field.getAnnotation(Column.class);
            if (column != null && !PrimaryKey.NOT_PK.equals(column.primaryKey())) {
                ids.add(field);
            }
        }
        if (ids.size() == 1) {
            info.idType = ids.get(0).getType().getSimpleName();
        }
        return info;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public String getEntityPackage() {
        return entityPackage;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getCamelCaseName() {
        return camelCaseName;
    }

    public String getIdType() {
        return idType;
    }
}
