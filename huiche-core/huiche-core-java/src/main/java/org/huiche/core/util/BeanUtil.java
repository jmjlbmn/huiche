package org.huiche.core.util;

import lombok.experimental.UtilityClass;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Bean工具类
 *
 * @author Maning
 */
@UtilityClass
public class BeanUtil {
    /**
     * 获取值是null的字段名称
     *
     * @param bean 对象
     * @return null的字段名称
     */
    @Nonnull
    public static String[] getNullFields(@Nullable Object bean) {
        List<Field> list = getNullFieldList(bean);
        List<String> result = new ArrayList<>();
        for (Field field : list) {
            if (!result.contains(field.getName())) {
                result.add(field.getName());
            }
        }
        return result.toArray(new String[0]);
    }

    /**
     * 获取值是null的字段
     *
     * @param bean 对象
     * @return null的字段
     */
    @Nonnull
    public static List<Field> getNullFieldList(@Nullable Object bean) {
        List<Field> result = new ArrayList<>();
        if (null == bean) {
            return result;
        }
        List<Field> fields = getFieldsWithSuper(bean.getClass());
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                if (field.get(bean) == null) {
                    result.add(field);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 获取类声明的私有字段(不含父类)
     *
     * @param clazz 类
     * @return 字段
     */
    @Nonnull
    public static List<Field> getFields(@Nonnull Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        List<Field> list = new ArrayList<>();
        for (Field field : fields) {
            int modifiers = field.getModifiers();
            if (Modifier.isPrivate(modifiers) && !Modifier.isFinal(modifiers) && !Modifier.isStatic(modifiers)) {
                list.add(field);
            }
        }
        return list;
    }

    /**
     * 获取类及其父类声明的所有私有字段
     *
     * @param clazz 类
     * @return 字段
     */
    @Nonnull
    public static List<Field> getFieldsWithSuper(@Nonnull Class clazz) {
        List<Field> fields = new ArrayList<>();
        while (!clazz.equals(Object.class)) {
            fields.addAll(getFields(clazz));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }
}
