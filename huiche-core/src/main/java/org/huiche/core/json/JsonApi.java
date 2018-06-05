package org.huiche.core.json;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 内置的Json转换接口,使用starter时会创建实现,直接注入即可,不使用start时,需要自行实现才可以使用
 *
 * @author Maning
 */
public interface JsonApi {
    /**
     * 对象转json
     *
     * @param object 对象
     * @return json字符串
     */
    @Nonnull
    String toJson(@Nullable Object object);

    /**
     * json字符串转对象
     *
     * @param json  json字符串
     * @param clazz 转换类型
     * @param <T>   类型
     * @return 对象
     */
    @Nonnull
    <T> T fromJson(@Nullable String json, @Nonnull Class<T> clazz);
}
