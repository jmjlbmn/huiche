package org.huiche.core.util;

import lombok.experimental.UtilityClass;
import org.huiche.core.enums.ValEnum;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * 枚举工具类,提供枚举转换等方法
 *
 * @author Maning
 */
@UtilityClass
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
    @Nullable
    public static <S extends Enum<S>, T extends Enum<T>> T turn(@Nullable S instance, @Nonnull Class<T> target) {
        if (null != instance) {
            for (T t : target.getEnumConstants()) {
                if (instance.name().equals(t.name())) {
                    return t;
                }
            }
        }
        return null;
    }

    /**
     * 值转枚举
     *
     * @param val       值
     * @param enumClazz 枚举类
     * @param <T>       枚举类型
     * @return 枚举
     */
    @Nullable
    public static <T extends Enum<T> & ValEnum> T of(@Nullable Integer val, @Nonnull Class<T> enumClazz) {
        if (null != val) {
            for (T t : enumClazz.getEnumConstants()) {
                if (Objects.equals(t.val(), val)) {
                    return t;
                }
            }
        }
        return null;
    }
}
