package org.huiche.core.service;


import org.huiche.core.entity.BaseEntity;
import org.huiche.core.page.PageRequest;
import org.huiche.core.page.PageResponse;

import java.util.List;

/**
 * @author Maning
 * @version 2017/7/12
 */
public interface BaseService<T extends BaseEntity> {
    /**
     * 保存
     *
     * @param entity 实体
     * @return ID
     */
    Long create(T entity);

    /**
     * 更新
     *
     * @param entity 实体
     * @return ID
     */
    Long update(T entity);

    /**
     * 删除
     *
     * @param id ID
     */
    void delete(Long id);

    /**
     * 删除
     *
     * @param id ID
     * @return 删除条数
     */
    Long delete(Long... id);

    /**
     * 删除
     *
     * @param id ID
     * @return 删除条数
     */
    Long delete(List<Long> id);

    /**
     * 删除
     *
     * @param ids ID
     * @return 删除条数
     */
    Long delete(String ids);

    /**
     * 保存数据
     *
     * @param entity 实体
     * @return ID
     */
    Long save(T entity);

    /**
     * 删除
     *
     * @param id ID
     * @return 删除数量
     */
    T get(Long id);

    /**
     * 获取数据列表
     *
     * @return 数据列表
     */
    List<T> list();

    /**
     * 获取数据列表
     *
     * @param search 检索
     * @return 数据列表
     */
    List<T> list(T search);

    /**
     * 获取数据分页
     *
     * @param pageRequest 分页
     * @return 数据
     */
    PageResponse<T> page(PageRequest pageRequest);

    /**
     * 获取数据分页
     *
     * @param search      检索
     * @param pageRequest 分页
     * @return 数据
     */
    PageResponse<T> page(PageRequest pageRequest, T search);

}
