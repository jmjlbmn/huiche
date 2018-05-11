package org.huiche.core.service;

import org.huiche.core.dao.BaseCrudDao;
import org.huiche.core.entity.BaseEntity;
import org.huiche.core.exception.Assert;
import org.huiche.core.exception.SystemError;
import org.huiche.core.page.PageRequest;
import org.huiche.core.page.PageResponse;
import org.huiche.core.search.Search;
import org.huiche.core.util.QueryUtil;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * 基础的增删改查业务实现
 *
 * @author Maning
 */
public abstract class BaseCrudServiceImpl<T extends BaseEntity> extends BaseServiceImpl implements BaseCrudService<T> {
    /**
     * 保存
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
     * 删除
     *
     * @param id ID
     * @return 删除条数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long delete(@Nonnull Long id) {
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
    public long delete(@Nonnull Long... id) {
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
     * 删除
     *
     * @param ids ID
     * @return 删除条数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long delete(@Nonnull String ids) {
        return dao().delete(ids);
    }

    /**
     * 根据ID获取一条数据
     *
     * @param id ID
     * @return 数据
     */
    @Override
    public T get(@Nonnull Long id) {
        T t = dao().get(id);
        Assert.notNull(SystemError.NO_EXISTS, t);
        return t;
    }

    /**
     * 根据ID获取数据
     *
     * @param ids 逗号分隔的ID
     * @return 删除数量
     */
    @Override
    @Nonnull
    public List<T> get(@Nonnull String ids) {
        return dao().list(ids);
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
    protected abstract BaseCrudDao<T> dao();
}
