package org.huiche.core.bean;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * bean信息,里面暂时只是包含了bean的所有属性信息,有一个方法的弱缓存
 *
 * @author Soap
 */
public class BeanInfo {
    /**
     * 方法缓存
     */
    protected static Map<Class<?>, Method[]> declaredMethodCache = new WeakHashMap<Class<?>, Method[]>();
    /**
     * 属性信息缓存
     */
    private static Map<Class<?>, PropertyInfo[]> declaredFieldCache = new WeakHashMap<Class<?>, PropertyInfo[]>();

    private Class<?> clazz;

    public BeanInfo(Class<?> clazz) {
        this.clazz = clazz;
        declaredMethodCache.put(clazz, getMethods(clazz));
        declaredFieldCache.put(clazz, getPropertyInfo(clazz));
    }

    /**
     * 获取这个bean的属性信息
     */
    public PropertyInfo[] getPropertyInfo() {
        PropertyInfo[] propertyInfo = null;
        propertyInfo = declaredFieldCache.get(clazz);
        if (propertyInfo == null) {
            propertyInfo = getPropertyInfo(clazz);
        }
        return propertyInfo;
    }

    /**
     * 静态方法,获取一个bean封装的属性信息,通过反射
     *
     * @param clazz 类型
     * @param <T>   泛型类型
     * @return 属性信息
     */
    public static <T> PropertyInfo[] getPropertyInfo(Class<T> clazz) {
        List<PropertyInfo> ps = new ArrayList<>();
        Field[] fields = getFields(clazz);
        for (Field field : fields) {
            if (field.getName().contains("$") || Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            PropertyInfo info = new PropertyInfo(clazz, field);
            ps.add(info);
        }
        return ps.toArray(new PropertyInfo[ps.size()]);
    }

    /**
     * 静态方法,获取一个bean封装的Field信息,通过反射
     *
     * @param clazz 类型
     * @param <T>   泛型类型
     * @return 字段信息
     */
    public static <T> Field[] getFields(Class<T> clazz) {
        List<Field> fleids = new ArrayList<>();
        for (Class<?> c = clazz; c != Object.class; c = c.getSuperclass()) {
            fleids.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return fleids.toArray(new Field[fleids.size()]);
    }

    /**
     * 静态方法,获取一个bean封装的Method信息,通过反射
     *
     * @param clazz 类型
     * @param <T>   泛型类型
     * @return 方法信息
     */
    public static <T> Method[] getMethods(Class<T> clazz) {
        List<Method> methods = new ArrayList<>();
        for (Class<?> c = clazz; c != Object.class; c = c.getSuperclass()) {
            methods.addAll(Arrays.asList(c.getDeclaredMethods()));
        }
        return methods.toArray(new Method[methods.size()]);
    }
}
