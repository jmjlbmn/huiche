package org.huiche.core.util;

import lombok.extern.slf4j.Slf4j;
import org.huiche.core.bean.BeanInfo;
import org.huiche.core.bean.PropertyInfo;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author Maning
 * @date 2017/12/5
 */
@Slf4j
public class ReflectUtil {
    private static final Map<String, Map<String, PropertyInfo>> CACHE = new WeakHashMap<>();

    public static Map<String, PropertyInfo> getPropertyMap(Class<?> clazz) {
        String className = clazz.getName();
        if (CACHE.containsKey(className)) {
            return CACHE.get(className);
        }
        Map<String, PropertyInfo> map = new WeakHashMap<>();
        for (PropertyInfo propertyInfo : getAllPropertyDescriptor(clazz)) {
            map.put(propertyInfo.getName(), propertyInfo);
        }
        CACHE.put(className, map);
        return map;
    }

    public static Type[] getActualTypes(Field field) {
        Type type = field.getGenericType();
        if (type instanceof ParameterizedType) {
            return ((ParameterizedType) type).getActualTypeArguments();
        }
        return null;
    }

    public static Type getActualType(Field field) {
        Type type = field.getGenericType();
        if (type instanceof ParameterizedType) {
            return ((ParameterizedType) type).getActualTypeArguments()[0];
        }
        return null;
    }


    public static List<Field> getAllField(Class<?> clazz) {
        List<Field> list = new ArrayList<>();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            list.addAll(Arrays.asList(clazz.getDeclaredFields()));
        }
        return list;
    }

    public static List<PropertyInfo> getAllPropertyDescriptor(Class<?> clazz) {
        return Arrays.asList(new BeanInfo(clazz).getPropertyInfo());
    }
}
