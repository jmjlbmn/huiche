package org.huiche.core.util;

import lombok.experimental.UtilityClass;
import org.huiche.core.annotation.consts.ConstVal;
import org.huiche.core.consts.ConstClass;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 常量类工具类
 *
 * @author Maning
 */
@UtilityClass
public class ConstUtil {
    public static <T extends ConstClass> List<org.huiche.core.consts.ConstVal> getValList(Class<T> constant) {
        if (null == constant) {
            return Collections.emptyList();
        }
        List<org.huiche.core.consts.ConstVal> list = new ArrayList<>();
        for (Field field : constant.getFields()) {
            ConstVal annotation = field.getAnnotation(ConstVal.class);
            try {
                list.add(org.huiche.core.consts.ConstVal.put(field.get(null).toString(), annotation.value()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static <T extends ConstClass> List<org.huiche.core.consts.ConstVal> getValListWithExtra(Class<T> constant) {
        if (null == constant) {
            return Collections.emptyList();
        }
        List<org.huiche.core.consts.ConstVal> list = new ArrayList<>();
        for (Field field : constant.getFields()) {
            ConstVal annotation = field.getAnnotation(ConstVal.class);
            try {
                list.add(org.huiche.core.consts.ConstVal.put(field.get(null).toString(), annotation.value(), annotation.extra()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static <T extends ConstClass> String getTextByValue(Class<T> constant, Object value) {
        for (org.huiche.core.consts.ConstVal val : getValList(constant)) {
            if (val.getValue().equals(value.toString())) {
                return val.getText();
            }
        }
        return value + "";
    }
}
