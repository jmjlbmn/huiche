package org.huiche.core.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 注解工具类
 *
 * @author Maning
 */
public class AnnotationUtil {
	public <T extends Annotation> Field getByAnnotation(Object obj, Class<T> annotationClass) {
		for (Field field : obj.getClass().getFields()) {
			T anno = field.getAnnotation(annotationClass);
			if (anno != null) {
				return field;
			}
		}
		return null;
	}

	public <T extends Annotation> List<Field> getListByAnnotation(Object obj, Class<T> annotationClass) {
		List<Field> list = new ArrayList<>();
		for (Field field : obj.getClass().getFields()) {
			T anno = field.getAnnotation(annotationClass);
			if (anno != null) {
				list.add(field);
			}
		}
		return list;

	}
}
