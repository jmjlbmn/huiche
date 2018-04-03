package org.huiche.core.service;


import org.huiche.core.entity.BaseEntity;
import org.huiche.core.page.PageRequest;
import org.huiche.core.page.PageResponse;
import org.huiche.core.search.Search;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * 基础的增删改查Service接口
 *
 * @author Maning
 */
public interface BaseCrudService<T extends BaseEntity> extends BaseService {
    /**
     * 保存
     *
     * @param entity 实体
     * @return 新增条数
     */
    long create(@Nonnull T entity);

    /**
     * 更新
     *
     * @param entity 实体
     * @return 更新条数
     */
    long update(@Nonnull T entity);

    /**
     * 删除
     *
     * @param id ID
     * @return 删除条数
     */
    long delete(@Nonnull Long id);

    /**
     * 删除
     *
     * @param id ID
     * @return 删除条数
     */
    long delete(@Nonnull Long... id);

    /**
     * 删除
     *
     * @param id ID
     * @return 删除条数
     */
    long delete(@Nonnull List<Long> id);

    /**
     * 删除
     *
     * @param ids ID
     * @return 删除条数
     */
    long delete(@Nonnull String ids);

    /**
     * 保存数据
     *
     * @param entity 实体
     * @return 更新条数
     */
    long save(@Nonnull T entity);

    /**
     * 根据ID获取一条数据
     *
     * @param id ID
     * @return 数据
     */
    T get(@Nonnull Long id);

    /**
     * 根据ID获取数据
     *
     * @param ids 逗号分隔的ID
     * @return 删除数量
     */
    List<T> get(@Nonnull String ids);

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
    List<T> list(@Nullable T search);

    /**
     * 获取数据分页
     *
     * @param pageRequest 分页
     * @return 数据
     */
    PageResponse<T> page(@Nullable PageRequest pageRequest);

    /**
     * 获取数据分页并简单筛选
     *
     * @param search      检索
     * @param pageRequest 分页
     * @return 数据
     */
    PageResponse<T> page(@Nullable PageRequest pageRequest, @Nullable T search);

    /**
     * 获取数据分页并自定义筛选
     *
     * @param search      检索
     * @param pageRequest 分页
     * @param <S>         搜索实现类
     * @return 数据
     */
    <S extends Search> PageResponse<T> page(@Nullable PageRequest pageRequest, @Nullable S search);

}
