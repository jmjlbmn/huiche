package org.huiche.core.validation;

import org.huiche.core.exception.HuiCheException;
import org.huiche.core.util.StringUtil;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author Maning
 */
public interface ValidationUtil {
    /**
     * 验证是否存在错误
     *
     * @param objects 验证对象
     * @return 错误消息
     */
    List<String> check(@Nonnull Object... objects);

    /**
     * 是否存在错误
     *
     * @param objects 验证对象
     * @return 是否存在错误
     */
    default boolean hasError(@Nonnull Object... objects) {
        return !check(objects).isEmpty();
    }

    /**
     * 是否没有错误
     *
     * @param objects 验证对象
     * @return 是否没有错误
     */
    default boolean ok(@Nonnull Object... objects) {
        return check(objects).isEmpty();
    }

    /**
     * 验证并抛出异常
     *
     * @param objects 验证对象
     */
    default void checkAndThrow(@Nonnull Object... objects) {
        List<String> result = check(objects);
        if (!result.isEmpty()) {
            throw new HuiCheException(StringUtil.join(result));
        }
    }
}
