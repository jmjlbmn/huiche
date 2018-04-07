package org.huiche.core.web.api;

import org.huiche.core.entity.BaseEntity;
import org.huiche.core.web.api.method.Del;
import org.huiche.core.web.api.method.Get;
import org.huiche.core.web.api.method.Save;

/**
 * 简单Restful风格控制器(非严格遵循规范,可自行根据业务实现编写 BaseController)
 *
 * @author Maning
 */
public abstract class BaseCrudApi<T extends BaseEntity<T>> implements Get<T>, Del<T>, Save<T> {
}
