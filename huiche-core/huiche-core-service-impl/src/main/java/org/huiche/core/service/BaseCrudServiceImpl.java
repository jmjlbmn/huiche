package org.huiche.core.service;

import org.huiche.core.dao.BaseDao;
import org.huiche.core.entity.BaseEntity;
import org.huiche.core.exception.Assert;
import org.huiche.core.exception.SystemError;
import org.huiche.core.page.PageRequest;
import org.huiche.core.page.PageResponse;
import org.huiche.core.search.Search;
import org.huiche.core.search.SearchUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 基础的增删改查业务实现
 *
 * @author Maning
 */
public class BaseCrudServiceImpl<T extends BaseEntity> extends BaseServiceImpl implements BaseCrudService<T> {
    /**
     * 获取dao
     */
    @Autowired
    protected BaseDao<T> dao;

    /**
     * 保存
     *
     * @param entity 实体
     * @return ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long create(T entity) {
        checkOnCreate(entity);
        checkRegular(entity);
        return dao.create(entity);
    }

    /**
     * 更新
     *
     * @param entity 实体
     * @return 更新条数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long update(T entity) {
        checkRegular(entity);
        return dao.update(entity);
    }

    /**
     * 删除
     *
     * @param id ID
     * @return 删除条数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long delete(Long id) {
        return dao.delete(id);
    }

    /**
     * 删除
     *
     * @param id ID
     * @return 删除条数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long delete(Long... id) {
        return dao.delete(id);
    }

    /**
     * 删除
     *
     * @param id ID
     * @return 删除条数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long delete(List<Long> id) {
        return dao.delete(id);
    }

    /**
     * 删除
     *
     * @param ids ID
     * @return 删除条数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long delete(String ids) {
        return dao.delete(ids);
    }

    /**
     * 根据ID获取一条数据
     *
     * @param id ID
     * @return 数据
     */
    @Override
    public T get(Long id) {
        T t = dao.get(id);
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
    public List<T> get(String ids) {
        return dao.list(ids);
    }

    /**
     * 获取数据列表
     *
     * @return 数据列表
     */
    @Override
    public List<T> list() {
        return dao.list();
    }

    /**
     * 获取数据列表
     *
     * @param search 检索
     * @return 数据列表
     */
    @Override
    public List<T> list(T search) {
        return dao.list(SearchUtil.ofEntity(search));
    }

    /**
     * 获取数据分页
     *
     * @param pageRequest 分页
     * @return 数据
     */
    @Override
    public PageResponse<T> page(PageRequest pageRequest) {
        return dao.page(pageRequest);
    }

    /**
     * 获取数据分页并简单筛选
     *
     * @param search      检索
     * @param pageRequest 分页
     * @return 数据
     */
    @Override
    public PageResponse<T> page(PageRequest pageRequest, T search) {
        return dao.page(pageRequest, SearchUtil.ofEntity(search));
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
    public <S extends Search> PageResponse<T> page(PageRequest pageRequest, S search) {
        return dao.page(pageRequest, search.get());
    }

    /**
     * 保存数据
     *
     * @param entity 实体
     * @return 更新条数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long save(T entity) {
        if (entity.getId() == null) {
            return dao.create(entity);
        } else {
            return dao.update(entity);
        }
    }

    /**
     * 创建之前的检查
     *
     * @param entity 实体
     */
    protected void checkOnCreate(T entity) {
        Assert.notNull(entity);
    }

    /**
     * 检查规则
     *
     * @param entity 实体
     */
    protected void checkRegular(T entity) {
        Assert.notNull(entity);
    }
}
