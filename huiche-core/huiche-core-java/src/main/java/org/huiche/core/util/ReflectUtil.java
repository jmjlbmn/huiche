package org.huiche.core.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.huiche.core.bean.BeanInfo;
import org.huiche.core.bean.PropertyInfo;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * 反射工具类
 *
 * @author Maning
 */
@Slf4j
@UtilityClass
public class ReflectUtil {
    private static final Map<String, Map<String, PropertyInfo>> CACHE = new WeakHashMap<>();

    /**
     * 获取类的属性map
     *
     * @param clazz 类
     * @return 熟悉map
     */
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

    /**
     * 获取字段的泛型类型
     *
     * @param field 字段
     * @return 泛型类型
     */
    public static Type[] getActualTypes(Field field) {
        Type type = field.getGenericType();
        if (type instanceof ParameterizedType) {
            return ((ParameterizedType) type).getActualTypeArguments();
        }
        return null;
    }

    /**
     * 获取字段的第一个泛型类型
     *
     * @param field 字段
     * @return 泛型类型
     */
    public static Type getActualType(Field field) {
        Type type = field.getGenericType();
        if (type instanceof ParameterizedType) {
            return ((ParameterizedType) type).getActualTypeArguments()[0];
        }
        return null;
    }

    /**
     * 获取类的属性信息列表
     *
     * @param clazz 类
     * @return 属性列表
     */
    public static List<PropertyInfo> getAllPropertyDescriptor(Class<?> clazz) {
        return Arrays.asList(new BeanInfo(clazz).getPropertyInfo());
    }
}
