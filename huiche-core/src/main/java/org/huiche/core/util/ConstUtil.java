package org.huiche.core.util;

import lombok.experimental.UtilityClass;
import org.huiche.annotation.consts.ConstVal;
import org.huiche.core.consts.ConstValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 常量类工具类,获取用@ConstVal注解的常类类参数的说明描述或扩展信息
 *
 * @author Maning
 */
@UtilityClass
public class ConstUtil {
    /**
     * 获取常量类的值和扩展数据
     *
     * @param constant 常量
     * @param <T>      常量类
     * @return 值和扩展数据
     */
    @Nonnull
    public static <T> List<ConstValue> list(@Nonnull Class<T> constant) {
        List<ConstValue> list = new ArrayList<>();
        for (Field field : constant.getFields()) {
            ConstVal annotation = field.getAnnotation(ConstVal.class);
            if (null != annotation) {
                try {
                    list.add(new ConstValue(field.get(null).toString(), annotation.value(), annotation.extra()));
                } catch (IllegalAccessException ignored) {
                }
            }
        }
        return list;
    }

    /**
     * 获取常量值的文字描述
     *
     * @param constant 常量
     * @param value    值
     * @param <T>      常量类
     * @return 描述
     */
    @Nonnull
    public static <T> String val(@Nonnull Class<T> constant, @Nullable Object value) {
        if (HuiCheUtil.isEmpty(value)) {
            return "";
        }
        for (ConstValue val : list(constant)) {
            if (val.getValue().equals(value.toString())) {
                return val.getText();
            }
        }
        return value.toString();
    }
}
