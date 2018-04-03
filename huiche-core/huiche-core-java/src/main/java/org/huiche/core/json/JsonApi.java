package org.huiche.core.json;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Json Api
 * @author Maning
 */
public interface JsonApi {
    /**
     * 对象转json
     *
     * @param object 对象
     * @return json字符串
     */
    @Nullable
    String toJson(@Nullable Object object);

    /**
     * json字符串转对象
     *
     * @param json  json字符串
     * @param clazz 转换类型
     * @param <T>   类型
     * @return 对象
     */
    @Nullable
    <T> T fromJson(@Nullable String json,@Nonnull Class<T> clazz);
}
