package org.huiche.dao.provider;

/**
 * 创建/新增数据的处理方法
 * @author Maning
 */
public interface CreateHandleProvider<T> extends UpdateHandleProvider<T> {
    /**
     * 创建时验证
     *
     * @param entity 实体
     */
    void validOnCreate(T entity);

    /**
     * 创建之前的处理(默认值等)
     *
     * @param entity 实体
     */
    void beforeCreate(T entity);

    /**
     * 新增数据时是否设置ID
     *
     * @return 是否
     */
    boolean createSetId();
}
