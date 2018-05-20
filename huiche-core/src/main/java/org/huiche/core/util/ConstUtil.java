package org.huiche.core.util;

import lombok.experimental.UtilityClass;
import org.huiche.annotation.consts.ConstVal;
import org.huiche.core.consts.ConstValue;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 常量类工具类
 *
 * @author Maning
 */
@UtilityClass
public class ConstUtil {
    /**
     * 获取常量类的常量值
     *
     * @param constant 常量类
     * @param <T>      常量类
     * @return 值
     */
    @Nonnull
    public static <T> List<ConstValue> getValList(@Nonnull Class<T> constant) {
        List<ConstValue> list = new ArrayList<>();
        for (Field field : constant.getFields()) {
            ConstVal annotation = field.getAnnotation(ConstVal.class);
            try {
                list.add(new ConstValue(field.get(null).toString(), annotation.value()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取常量类的值和扩展数据
     *
     * @param constant 常量
     * @param <T>      常量类
     * @return 值和扩展数据
     */
    @Nonnull
    public static <T> List<ConstValue> getValListWithExtra(@Nonnull Class<T> constant) {
        List<ConstValue> list = new ArrayList<>();
        for (Field field : constant.getFields()) {
            ConstVal annotation = field.getAnnotation(ConstVal.class);
            try {
                list.add(new ConstValue(field.get(null).toString(), annotation.value(), annotation.extra()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
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
    public static <T> String getTextByValue(@Nonnull Class<T> constant, @Nonnull Object value) {
        for (ConstValue val : getValList(constant)) {
            if (val.getValue().equals(value.toString())) {
                return val.getText();
            }
        }
        return value + "";
    }
}
