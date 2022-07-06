package org.huiche.dao.operation;

import org.springframework.lang.Nullable;

/**
 * @author Maning
 */
public interface CrudOperation<T> extends CreateOperation<T>, DeleteOperation<T>, UpdateOperation<T>, GetOperation<T>, ListOperation<T>, PageOperation<T>, CountOperation<T>, ExistsOperation<T> {
    /**
     * 创建之前回调,可设置默认值等操作
     *
     * @param entity 实体
     */
    default void beforeCreate(T entity) {
    }

    /**
     * 更新之前回调,可设置默认值, 注意,传入参数可能为null
     *
     * @param entity 实体
     * @return 实体
     */
    @Nullable
    default T beforeUpdate(@Nullable T entity) {
        return entity;
    }
}
