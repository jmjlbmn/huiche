package org.huiche.core.util;

import org.huiche.core.enums.ValEnum;

import java.util.Objects;

/**
 * 枚举工具类
 *
 * @author Maning
 */
public class EnumUtil {
    /**
     * 枚举转换
     *
     * @param instance 要转换的枚举实例
     * @param target   想要转换成的枚举
     * @param <S>      原枚举
     * @param <T>      目标枚举
     * @return 转换后的枚举类型
     */
    public static <S extends Enum<S>, T extends Enum<T>> T turn(S instance, Class<T> target) {
        if (null != instance) {
            for (T t : target.getEnumConstants()) {
                if (instance.name().equals(t.name())) {
                    return t;
                }
            }
        }
        return null;
    }

    public static <T extends Enum<T> & ValEnum> T of(Integer val, Class<T> enumClazz) {
        if (null != val && null != enumClazz) {
            for (T t : enumClazz.getEnumConstants()) {
                if (Objects.equals(t.val(), val)) {
                    return t;
                }
            }
        }
        return null;
    }
}
