package org.huiche.core.util;

import lombok.experimental.UtilityClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 注解工具类
 *
 * @author Maning
 */
@UtilityClass
public class AnnotationUtil {
    /**
     * 获取对象标注有指定注解的第一个字段
     *
     * @param obj             对象
     * @param annotationClass 注解
     * @param <T>             注解类型
     * @return 字段
     */
    public <T extends Annotation> Field getByAnnotation(Object obj, Class<T> annotationClass) {
        for (Field field : obj.getClass().getFields()) {
            T annotation = field.getAnnotation(annotationClass);
            if (annotation != null) {
                return field;
            }
        }
        return null;
    }

    /**
     * 获取对象标注有指定注解的所有字段
     *
     * @param obj             对象
     * @param annotationClass 注解
     * @param <T>             注解类型
     * @return 字段
     */
    public <T extends Annotation> List<Field> getListByAnnotation(Object obj, Class<T> annotationClass) {
        List<Field> list = new ArrayList<>();
        for (Field field : obj.getClass().getFields()) {
            T annotation = field.getAnnotation(annotationClass);
            if (annotation != null) {
                list.add(field);
            }
        }
        return list;
    }
}
