package org.huiche.dao.provider;

/**
 * @author Maning
 */
public interface UpdateHandleProvider<T> {
    /**
     * 验证规则(主要是字段长度等)
     *
     * @param entity 实体
     */
    void validRegular(T entity);

    /**
     * 更新之前进行处理
     *
     * @param entity 实体
     */
    void beforeUpdate(T entity);
}
