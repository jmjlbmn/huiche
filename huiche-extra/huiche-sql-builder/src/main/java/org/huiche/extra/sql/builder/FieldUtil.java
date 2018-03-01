package org.huiche.extra.sql.builder;


import org.huiche.core.annotation.Column;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Maning
 */
public class FieldUtil {
    public static List<Field> getField(Class<?> clazz) {
        Map<String, Field> map = new HashMap<>(16);
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            for (Field field : Arrays.asList(clazz.getDeclaredFields())) {
                int modifiers = field.getModifiers();
                if (Modifier.isPrivate(modifiers) && !Modifier.isFinal(modifiers) && !Modifier.isStatic(modifiers)) {
                    if (!map.containsKey(field.getName())) {
                        map.put(field.getName(), field);
                    }
                }
            }
        }
        return map.entrySet().stream().map(Map.Entry::getValue).sorted((a, b) -> {
            Column colA = a.getAnnotation(Column.class);
            Column colB = b.getAnnotation(Column.class);
            if (null != colA && null != colB) {
                int pk = 2;
                int notNull = 1;
                int sumA = (colA.isPrimaryKey() ? pk : 0) + (colA.notNull() ? notNull : 0);
                int sumB = (colB.isPrimaryKey() ? pk : 0) + (colB.notNull() ? notNull : 0);
                return sumB - sumA;
            }
            if (null == colA) {
                return 1;
            } else {
                return -1;
            }
        }).collect(Collectors.toList());
    }
}
