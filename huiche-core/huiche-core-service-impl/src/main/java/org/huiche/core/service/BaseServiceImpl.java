package org.huiche.core.service;

import org.huiche.core.dao.BaseDao;
import org.huiche.core.entity.BaseEntity;
import org.huiche.core.exception.Assert;
import org.huiche.core.page.PageRequest;
import org.huiche.core.page.PageResponse;
import org.huiche.core.util.QueryDslUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Maning
 */
@Transactional(rollbackFor = Exception.class, readOnly = true)
public abstract class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {
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
    public Long create(T entity) {
        checkOnCreate(entity);
        checkRegular(entity);
        return dao.create(entity);
    }

    /**
     * 更新
     *
     * @param entity 实体
     * @return ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long update(T entity) {
        checkRegular(entity);
        return dao.update(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        dao.delete(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long delete(Long... id) {
        return dao.delete(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long delete(List<Long> id) {
        return dao.delete(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long delete(String ids) {
        return dao.delete(ids);
    }

    @Override
    public T get(Long id) {
        return dao.get(id);
    }

    @Override
    public List<T> list() {
        return dao.list();
    }

    @Override
    public List<T> list(T search) {
        return dao.list(QueryDslUtil.parseSearch(search));
    }

    @Override
    public PageResponse<T> page(PageRequest pageRequest) {
        return dao.page(pageRequest);
    }

    @Override
    public PageResponse<T> page(PageRequest pageRequest, T search) {
        return dao.page(pageRequest, QueryDslUtil.parsePageRequest(search, pageRequest), QueryDslUtil.parseSearch(search));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(T entity) {
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
