package org.huiche.core.service;


import org.huiche.core.entity.BaseEntity;

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
	 */
	void delete(Long... id);

	/**
	 * 删除
	 *
	 * @param id ID
	 */
	void delete(List<Long> id);

	/**
	 * 删除
	 *
	 * @param ids ID
	 */
	void delete(String ids);

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

}
