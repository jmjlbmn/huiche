package org.huiche.core.service;

import org.huiche.core.dao.BaseDao;
import org.huiche.core.entity.BaseEntity;
import org.huiche.core.exception.Assert;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Maning
 */
@Transactional(rollbackFor = Exception.class, readOnly = true)
public abstract class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {
	/**
	 * 获取dao
	 *
	 * @return dao
	 */
	protected abstract BaseDao<T> getBaseDao();

	/**
	 * 保存
	 *
	 * @param entity 实体
	 * @return ID
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long create(T entity) {
		checkNull(entity);
		checkRegular(entity);
		return getBaseDao().create(entity);
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
		return getBaseDao().update(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long id) {
		getBaseDao().delete(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long... id) {
		getBaseDao().delete(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(List<Long> id) {
		getBaseDao().delete(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(String ids) {
		getBaseDao().delete(ids);
	}

	@Override
	public T get(Long id) {
		return getBaseDao().get(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long save(T entity) {
		if (entity.getId() == null) {
			return getBaseDao().create(entity);
		} else {
			return getBaseDao().update(entity);
		}
	}

	protected void checkNull(T entity) {
		Assert.notNull(entity);
	}

	protected void checkRegular(T entity) {
		Assert.notNull(entity);
	}
}
