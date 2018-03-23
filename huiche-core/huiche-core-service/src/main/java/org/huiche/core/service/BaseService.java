package org.huiche.core.service;


import org.huiche.core.entity.BaseEntity;
import org.huiche.core.page.PageRequest;
import org.huiche.core.page.PageResponse;

import java.util.List;

/**
 * @author Maning
 */
public interface BaseService<T extends BaseEntity> {
    /**
     * 保存
     *
     * @param entity 实体
     * @return 新增条数
     */
    long create(T entity);

    /**
     * 更新
     *
     * @param entity 实体
     * @return 更新条数
     */
    long update(T entity);

    /**
     * 删除
     *
     * @param id ID
     * @return 删除条数
     */
    long delete(Long id);

    /**
     * 删除
     *
     * @param id ID
     * @return 删除条数
     */
    long delete(Long... id);

    /**
     * 删除
     *
     * @param id ID
     * @return 删除条数
     */
    long delete(List<Long> id);

    /**
     * 删除
     *
     * @param ids ID
     * @return 删除条数
     */
    long delete(String ids);

    /**
     * 保存数据
     *
     * @param entity 实体
     * @return 更新条数
     */
    long save(T entity);

    /**
     * 删除
     *
     * @param id ID
     * @return 数据
     */
    T get(Long id);
    /**
     * 删除
     *
     * @param ids 逗号分隔的ID
     * @return 删除数量
     */
    List<T> get(String ids);

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
