package org.huiche.web.api;

import org.huiche.data.entity.BaseEntity;
import org.huiche.web.api.method.Create;
import org.huiche.web.api.method.Del;
import org.huiche.web.api.method.Get;
import org.huiche.web.api.method.Update;

/**
 * 简单Restful风格增删改查控制器(非严格遵循规范,可自行根据业务实现编写 BaseController)
 *
 * @author Maning
 */
public abstract class BaseCrudApi<T extends BaseEntity<T>> implements Create<T>, Del<T>, Update<T>, Get<T> {
}
