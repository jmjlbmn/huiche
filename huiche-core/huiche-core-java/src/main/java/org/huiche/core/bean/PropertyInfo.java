package org.huiche.core.bean;


import org.huiche.core.util.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 属性信息,现在只包含 field,名称,读取方法,写入方法
 *
 * @author Soap
 */
public class PropertyInfo {

    private static final String GET = "get";
    private static final String SET = "set";
    private static final String IS = "is";

    private Method readMethod;
    private Method writeMethod;
    private final Field field;
    private final String name;

    PropertyInfo(Class<?> clazz, Field field) {
        this.field = field;
        Method[] methods = BeanInfo.DECLARED_METHOD_CACHE.get(clazz);
        Class<?> type = field.getType();
        name = field.getName();
        String fileName = StringUtil.convertFirstToUpperCase(name);
        String isReadMethodName = PropertyInfo.IS + fileName;
        String getReadMethodName = PropertyInfo.GET + fileName;

        String writeMethodName = PropertyInfo.SET + fileName;

        for (Method method : methods) {
            if (type == Boolean.class) {
                if (getReadMethodName.equals(method.getName()) || isReadMethodName.equals(method.getName())) {
                    readMethod = method;
                }
            } else {
                if (getReadMethodName.equals(method.getName())) {
                    readMethod = method;
                }
            }

            if (writeMethodName.equals(method.getName())) {
                writeMethod = method;
            }
            if (readMethod != null && writeMethod != null) {
                break;
            }
        }
    }

    public String getName() {
        return name;
    }

    public Method getReadMethod() {
        return readMethod;
    }

    public Method getWriteMethod() {
        return writeMethod;
    }

    public Field getField() {
        return field;
    }
}
