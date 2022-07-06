package org.huiche.dao.provider;

/**
 * 更新数据操作的处理方法接口
 *
 * @author Maning
 */
public interface UpdateHandleProvider<T> {

    /**
     * 更新之前进行处理
     *
     * @param entity 实体
     */
    void beforeUpdate(T entity);
}
