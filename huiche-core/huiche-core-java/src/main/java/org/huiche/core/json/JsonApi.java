package org.huiche.core.json;

/**
 * @author Maning
 */
public interface JsonApi {
    /**
     * 对象转json
     *
     * @param object 对象
     * @return json字符串
     */
    String toJson(Object object);

    /**
     * json字符串转对象
     *
     * @param json  json字符串
     * @param clazz 转换类型
     * @param <T>   类型
     * @return 对象
     */
    <T> T fromJson(String json, Class<T> clazz);
}
