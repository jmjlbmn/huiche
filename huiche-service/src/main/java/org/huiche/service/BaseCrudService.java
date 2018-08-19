package org.huiche.service;


import org.huiche.data.entity.BaseEntity;
import org.huiche.data.page.PageRequest;
import org.huiche.data.page.PageResponse;
import org.huiche.data.search.Search;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

/**
 * 基础的增删改查业务接口
 *
 * @author Maning
 */
public interface BaseCrudService<T extends BaseEntity<T>> extends BaseService {
    /**
     * 保存
     *
     * @param entity 实体
     * @return 新增条数
     */
    long create(@Nonnull T entity);

    /**
     * 保存
     *
     * @param entityList 实体
     * @return 新增条数
     */
    long create(@Nonnull Collection<T> entityList);

    /**
     * 更新
     *
     * @param entity 实体
     * @return 更新条数
     */
    long update(@Nonnull T entity);

    /**
     * 更新,包括NULL
     *
     * @param entity 实体
     * @return 更新条数
     */
    long updateWidthNull(@Nonnull T entity);

    /**
     * 删除
     *
     * @param id ID
     * @return 删除条数
     */
    long delete(long id);

    /**
     * 删除
     *
     * @param id ID
     * @return 删除条数
     */
    long delete(@Nonnull List<Long> id);

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
    @Nonnull
    T get(long id);

    /**
     * 获取数据列表
     *
     * @return 数据列表
     */
    @Nonnull
    List<T> list();

    /**
     * 获取数据列表
     *
     * @param search 检索
     * @return 数据列表
     */
    @Nonnull
    List<T> list(@Nullable T search);

    /**
     * 获取数据列表
     *
     * @param search 检索
     * @param <S>    搜索实现类
     * @return 数据列表
     */
    @Nonnull
    <S extends Search> List<T> list(@Nullable S search);

    /**
     * 获取数据分页
     *
     * @param pageRequest 分页
     * @return 数据
     */
    @Nonnull
    PageResponse<T> page(@Nullable PageRequest pageRequest);

    /**
     * 获取数据分页并简单筛选
     *
     * @param search      检索
     * @param pageRequest 分页
     * @return 数据
     */
    @Nonnull
    PageResponse<T> page(@Nullable PageRequest pageRequest, @Nullable T search);

    /**
     * 获取数据分页并自定义筛选
     *
     * @param search      检索
     * @param pageRequest 分页
     * @param <S>         搜索实现类
     * @return 数据
     */
    @Nonnull
    <S extends Search> PageResponse<T> page(@Nullable PageRequest pageRequest, @Nullable S search);

}
