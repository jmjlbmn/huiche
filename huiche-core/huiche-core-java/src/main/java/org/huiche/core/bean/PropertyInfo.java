package org.huiche.core.bean;


import org.huiche.core.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
    @NotNull
    private final Field field;
    private final String name;
    private Method readMethod;
    private Method writeMethod;

    PropertyInfo(@Nonnull Class<?> clazz, @Nonnull Field field) {
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

    /**
     * 获取属性名
     *
     * @return 属性名
     */
    @Nonnull
    public String getName() {
        return name;
    }

    /**
     * 获取get方法
     *
     * @return get方法
     */
    @Nullable
    public Method getReadMethod() {
        return readMethod;
    }

    /**
     * 获取set方法
     *
     * @return set方法
     */
    @Nullable
    public Method getWriteMethod() {
        return writeMethod;
    }

    /**
     * 获取字段
     *
     * @return 字段
     */
    @Nonnull
    public Field getField() {
        return field;
    }
}
