package org.huiche.web.api;


import org.huiche.data.entity.BaseEntity;
import org.huiche.web.api.method.Page;

/**
 * 简单Restful风格控制器(非严格遵循规范,可自行根据业务实现编写 BaseController)
 *
 * @author Maning
 */
public abstract class BaseCrudPageApi<T extends BaseEntity<T>> extends BaseCrudApi<T> implements Page<T> {
}
