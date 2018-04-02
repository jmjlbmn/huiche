package org.huiche.core.web.api;

import org.huiche.core.entity.BaseEntity;
import org.huiche.core.web.api.method.Page;

/**
 * 简单Restful风格控制器(非严格遵循规范,可自行根据业务实现编写 BaseController)
 *
 * @author Maning
 */
public abstract class BaseCrudPageApi<T extends BaseEntity> extends BaseCrudApi<T> implements Page<T> {
}
