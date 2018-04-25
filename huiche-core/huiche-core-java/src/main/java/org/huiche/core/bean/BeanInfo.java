package org.huiche.core.bean;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * bean信息,里面暂时只是包含了bean的所有属性信息,有一个方法的弱缓存
 *
 * @author Soap
 */
public class BeanInfo {
    /**
     * 方法缓存
     */
    static final Map<Class<?>, Method[]> DECLARED_METHOD_CACHE = new WeakHashMap<>();
    /**
     * 属性信息缓存
     */
    private static final Map<Class<?>, PropertyInfo[]> DECLARED_FIELD_CACHE = new WeakHashMap<>();

    @Nonnull
    private final Class<?> clazz;

    public BeanInfo(@Nonnull Class<?> clazz) {
        this.clazz = clazz;
        DECLARED_METHOD_CACHE.put(clazz, getMethods(clazz));
        DECLARED_FIELD_CACHE.put(clazz, getPropertyInfo(clazz));
    }

    /**
     * 静态方法,获取一个bean封装的属性信息,通过反射
     *
     * @param clazz 类型
     * @param <T>   泛型类型
     * @return 属性信息
     */
    @Nonnull
    public static <T> PropertyInfo[] getPropertyInfo(@Nonnull Class<T> clazz) {
        List<PropertyInfo> ps = new ArrayList<>();
        Field[] fields = getFields(clazz);
        for (Field field : fields) {
            if (field.getName().contains("$") || Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            PropertyInfo info = new PropertyInfo(clazz, field);
            ps.add(info);
        }
        return ps.toArray(new PropertyInfo[0]);
    }

    /**
     * 静态方法,获取一个bean封装的Field信息,通过反射
     *
     * @param clazz 类型
     * @param <T>   泛型类型
     * @return 字段信息
     */
    @Nonnull
    public static <T> Field[] getFields(@Nonnull Class<T> clazz) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> c = clazz; c != Object.class; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return fields.toArray(new Field[0]);
    }

    /**
     * 静态方法,获取一个bean封装的Method信息,通过反射
     *
     * @param clazz 类型
     * @param <T>   泛型类型
     * @return 方法信息
     */
    @Nonnull
    public static <T> Method[] getMethods(@Nonnull Class<T> clazz) {
        List<Method> methods = new ArrayList<>();
        for (Class<?> c = clazz; c != Object.class; c = c.getSuperclass()) {
            methods.addAll(Arrays.asList(c.getDeclaredMethods()));
        }
        return methods.toArray(new Method[0]);
    }

    /**
     * 获取这个bean的属性信息
     *
     * @return 属性信息
     */
    @Nonnull
    public PropertyInfo[] getPropertyInfo() {
        PropertyInfo[] propertyInfo;
        propertyInfo = DECLARED_FIELD_CACHE.get(clazz);
        if (propertyInfo == null) {
            propertyInfo = getPropertyInfo(clazz);
        }
        return propertyInfo;
    }
}
