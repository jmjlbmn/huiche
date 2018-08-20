package org.huiche.extra.sql.builder;

import lombok.extern.slf4j.Slf4j;
import org.huiche.annotation.sql.Column;
import org.huiche.extra.sql.builder.info.FieldColumn;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 建表工具用到的工具类
 *
 * @author Maning
 */
@Slf4j
public class Util {
    private static final List<Class> SUPPORT_TYPE_LIST = Arrays.asList(
            Boolean.class,
            Integer.class,
            Long.class,
            Float.class,
            Double.class,
            String.class);

    /**
     * 获取泛型map
     *
     * @param clazz 类
     * @return 泛型map
     */
    @Nonnull
    public static Map<String, Class<?>> getParameterizedTypeMap(@Nonnull Class<?> clazz) {
        if (clazz != Object.class) {
            Type type = clazz.getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                TypeVariable[] typeVariable = clazz.getSuperclass().getTypeParameters();
                Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
                Map<String, Class<?>> map = new HashMap<>(4);
                for (int i = 0; i < typeVariable.length; i++) {
                    try {
                        map.put(typeVariable[i].getName(), Class.forName(actualTypeArguments[i].getTypeName()));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                return map;
            } else {
                return getParameterizedTypeMap(clazz.getSuperclass());
            }
        }
        return Collections.emptyMap();
    }

    /**
     * 处理字段,排序等
     *
     * @param clazz     类
     * @param fieldList 字段list
     */
    public static void handleField(@Nonnull Class<?> clazz, @Nonnull List<Field> fieldList) {
        if (clazz != Object.class) {
            Field[] fields = clazz.getDeclaredFields();
            pull(fields, fieldList);
            handleField(clazz.getSuperclass(), fieldList);
        }
    }

    /**
     * 获取字段信息
     *
     * @param clazz 类
     * @return 字段信息
     */
    @Nonnull
    public static List<FieldColumn> getField(@Nonnull Class<?> clazz) {
        Map<String, Field> map = new LinkedHashMap<>(16);
        List<Field> fieldList = new ArrayList<>();
        handleField(clazz, fieldList);
        Collections.reverse(fieldList);
        for (Field field : fieldList) {
            //去重,让子类Field覆盖父类
            map.put(field.getName(), field);
        }
        List<FieldColumn> list = new ArrayList<>();
        for (String key : map.keySet()) {
            Field field = map.get(key);
            Class type = field.getType();
            if (SUPPORT_TYPE_LIST.contains(type) || type.isEnum()) {
                list.add(new FieldColumn(key, type, field.getAnnotation(Column.class)));
            } else {
                if (field.getType().getName().equals(field.getGenericType().getTypeName())) {
                    log.info("类 " + clazz.getName() + "的 " + key + " 属性不支持处理,已跳过");
                } else {
                    Map<String, Class<?>> parameterizedTypeMap = getParameterizedTypeMap(clazz);
                    String parameterizedType = field.getGenericType().getTypeName();
                    if (parameterizedTypeMap.containsKey(parameterizedType)) {
                        list.add(new FieldColumn(key, parameterizedTypeMap.get(parameterizedType), field.getAnnotation(Column.class)));
                    } else {
                        log.info("类 " + clazz.getName() + "的 " + key + " 属性不支持处理,已跳过");
                    }
                }
            }
        }
        return list;
    }

    private static boolean isInvalid(@Nonnull Field field) {
        int modifiers = field.getModifiers();
        return Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers);
    }

    private static void pull(@Nonnull Field[] src, @Nonnull List<Field> target) {
        if (src.length > 0) {
            for (int i = src.length - 1; i >= 0; i--) {
                // 倒序添加进去
                if (!isInvalid(src[i])) {
                    target.add(src[i]);
                }
            }
        }
    }
}
