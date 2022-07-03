package org.huiche.dao.operation;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Maning
 */
public interface CreateOperation<T> {
    /**
     * 新增
     *
     * @param entity 实体
     * @return 持久化后的实体
     */
    @NotNull T create(@NotNull T entity);

    /**
     * 新增或替换
     *
     * @param entity 实体
     * @return 持久化后的实体
     */
    @NotNull T createOrReplace(@NotNull T entity);

    /**
     * 批量插入
     *
     * @param entityArr 实体数组
     * @return 插入条数
     */
    default long createBatch(@NotNull T[] entityArr) {
        return createBatch(Arrays.asList(entityArr));
    }

    /**
     * 批量插入
     *
     * @param entityList 实体列表
     * @return 插入条数
     */
    long createBatch(@NotNull Collection<T> entityList);
}
