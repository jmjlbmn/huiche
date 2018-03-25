package org.huiche.core.service;

import org.huiche.core.dao.BaseDao;
import org.huiche.core.entity.BaseEntity;
import org.huiche.core.exception.Assert;
import org.huiche.core.page.PageRequest;
import org.huiche.core.page.PageResponse;
import org.huiche.core.search.Search;
import org.huiche.core.search.SearchUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long delete(Long id) {
        return dao.delete(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long delete(Long... id) {
        return dao.delete(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long delete(List<Long> id) {
        return dao.delete(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long delete(String ids) {
        return dao.delete(ids);
    }

    @Override
    public T get(Long id) {
        return dao.get(id);
    }

    @Override
    public List<T> get(String ids) {
        return dao.list(ids);
    }

    @Override
    public List<T> list() {
        return dao.list();
    }

    @Override
    public List<T> list(T search) {
        return dao.list(SearchUtil.ofEntity(search));
    }

    @Override
    public PageResponse<T> page(PageRequest pageRequest) {
        return dao.page(pageRequest);
    }

    @Override
    public PageResponse<T> page(PageRequest pageRequest, T search) {
        return dao.page(pageRequest, SearchUtil.ofEntity(search));
    }

    @Override
    public <S extends Search> PageResponse<T> page(PageRequest pageRequest, S search) {
        return dao.page(pageRequest,search.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long save(T entity) {
        if (entity.getId() == null) {
            return dao.create(entity);
        } else {
            return dao.update(entity);
        }
    }

    protected void checkOnCreate(T entity) {
        Assert.notNull(entity);
    }

    protected void checkRegular(T entity) {
        Assert.notNull(entity);
    }
}
