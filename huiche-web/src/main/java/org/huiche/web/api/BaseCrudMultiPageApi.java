package org.huiche.web.api;


import org.huiche.data.entity.BaseEntity;
import org.huiche.web.api.method.DelMulti;

/**
 * 简单Restful风格增删改查,批量删除分页接口 控制器
 *
 * @author Maning
 */
public abstract class BaseCrudMultiPageApi<T extends BaseEntity<T>> extends BaseCrudPageApi<T> implements DelMulti<T> {
}
