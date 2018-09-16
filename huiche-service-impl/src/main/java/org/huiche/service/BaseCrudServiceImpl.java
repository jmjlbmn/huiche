package org.huiche.service;

import org.huiche.core.exception.HuiCheError;
import org.huiche.core.util.Assert;
import org.huiche.dao.provider.BaseCrudDaoProvider;
import org.huiche.dao.util.QueryUtil;
import org.huiche.data.entity.BaseEntity;
import org.huiche.data.page.PageRequest;
import org.huiche.data.page.PageResponse;
import org.huiche.data.search.Search;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

/**
 * 基础的增删改查业务实现,继承BaseServiceImpl
 *
 * @author Maning
 */
public abstract class BaseCrudServiceImpl<T extends BaseEntity<T>> extends BaseServiceImpl implements BaseCrudService<T> {
    /**
     * 保存,新增
     *
     * @param entity 实体
     * @return ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long create(@Nonnull T entity) {
        beforeCreate(entity);
        checkOnCreate(entity);
        checkRegular(entity);
        return dao().create(entity);
    }

    /**
     * 批量保存,新增
     *
     * @param entityList 实体
     * @return 条数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long create(@Nonnull Collection<T> entityList) {
        entityList.forEach(
                entity -> {
                    beforeCreate(entity);
                    checkOnCreate(entity);
                    checkRegular(entity);
                }
        );
        return dao().create(entityList);
    }

    /**
     * 更新
     *
     * @param entity 实体
     * @return 更新条数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long update(@Nonnull T entity) {
        beforeUpdate(entity);
        checkRegular(entity);
        return dao().update(entity);
    }

    /**
     * 更新,包括NULL
     *
     * @param entity 实体
     * @return 更新条数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long updateWidthNull(@Nonnull T entity) {
        beforeCreate(entity);
        checkOnCreate(entity);
        checkRegular(entity);
        return dao().update(entity, false);
    }

    /**
     * 删除
     *
     * @param id ID
     * @return 删除条数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long delete(long id) {
        return dao().delete(id);
    }

    /**
     * 删除
     *
     * @param id ID
     * @return 删除条数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long delete(@Nonnull List<Long> id) {
        return dao().delete(id);
    }

    /**
     * 根据ID获取一条数据
     *
     * @param id ID
     * @return 数据
     */
    @Override
    @Nonnull
    public T get(long id) {
        T t = dao().get(id);
        Assert.notNull(HuiCheError.NO_EXISTS, t);
        return t;
    }

    /**
     * 获取数据列表
     *
     * @return 数据列表
     */
    @Override
    @Nonnull
    public List<T> list() {
        return dao().list();
    }

    /**
     * 获取数据列表
     *
     * @param search 检索
     * @return 数据列表
     */
    @Override
    @Nonnull
    public List<T> list(@Nullable T search) {
        return dao().list(QueryUtil.ofEntity(search));
    }

    @Nonnull
    @Override
    public <S extends Search> List<T> list(@Nullable S search) {
        return dao().list(null == search ? null : search.get());
    }

    /**
     * 获取数据分页
     *
     * @param pageRequest 分页
     * @return 数据
     */
    @Override
    @Nonnull
    public PageResponse<T> page(@Nullable PageRequest pageRequest) {
        return dao().page(pageRequest);
    }

    /**
     * 获取数据分页并简单筛选
     *
     * @param search      检索
     * @param pageRequest 分页
     * @return 数据
     */
    @Override
    @Nonnull
    public PageResponse<T> page(@Nullable PageRequest pageRequest, @Nullable T search) {
        return dao().page(pageRequest, QueryUtil.ofEntity(search));
    }

    /**
     * 获取数据分页并自定义筛选
     *
     * @param search      检索
     * @param pageRequest 分页
     * @param <S>         搜索实现类
     * @return 数据
     */
    @Override
    @Nonnull
    public <S extends Search> PageResponse<T> page(@Nullable PageRequest pageRequest, @Nullable S search) {
        return dao().page(pageRequest, null == search ? null : search.get());
    }

    /**
     * 保存数据
     *
     * @param entity 实体
     * @return 更新条数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long save(@Nonnull T entity) {
        if (entity.getId() == null) {
            return create(entity);
        } else {
            return update(entity);
        }
    }

    /**
     * 创建之前的检查
     *
     * @param entity 实体
     */
    protected void checkOnCreate(@Nonnull T entity) {

    }

    /**
     * 检查规则
     *
     * @param entity 实体
     */
    protected void checkRegular(@Nonnull T entity) {

    }

    /**
     * 创建之前
     *
     * @param entity 实体
     */
    protected void beforeCreate(@Nonnull T entity) {
    }

    /**
     * 更新之前
     *
     * @param entity 实体
     */
    protected void beforeUpdate(@Nonnull T entity) {
    }

    /**
     * 提供dao
     *
     * @return dao
     */
    protected abstract BaseCrudDaoProvider<T> dao();
}
