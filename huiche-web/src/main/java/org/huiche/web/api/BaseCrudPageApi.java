package org.huiche.web.api;


import org.huiche.data.entity.BaseEntity;
import org.huiche.web.api.method.Page;

/**
 * 简单Restful风格增删改查分页接口 控制器
 *
 * @author Maning
 */
public abstract class BaseCrudPageApi<T extends BaseEntity<T>> extends BaseCrudApi<T> implements Page<T> {
}
