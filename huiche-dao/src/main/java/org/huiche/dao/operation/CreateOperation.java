package org.huiche.dao.operation;

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
    <E extends T> E create(E entity);

    /**
     * 新增或替换
     *
     * @param entity 实体
     * @return 持久化后的实体
     */
    <E extends T> E createOrReplace(E entity);

    /**
     * 批量插入
     *
     * @param entityArr 实体数组
     * @return 插入条数
     */
    default <E extends T> long createBatch(E[] entityArr) {
        return createBatch(Arrays.asList(entityArr));
    }

    /**
     * 批量插入
     *
     * @param entityList 实体列表
     * @return 插入条数
     */
    <E extends T> long createBatch(Collection<E> entityList);
}
