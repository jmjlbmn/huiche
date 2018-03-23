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
    public <T extends Annotation> Field getByAnnotation(Object obj, Class<T> annotationClass) {
        for (Field field : obj.getClass().getFields()) {
            T annotation = field.getAnnotation(annotationClass);
            if (annotation != null) {
                return field;
            }
        }
        return null;
    }

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
