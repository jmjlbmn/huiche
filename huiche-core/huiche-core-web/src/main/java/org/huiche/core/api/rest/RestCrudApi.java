package org.huiche.core.api.rest;

import org.huiche.core.api.Del;
import org.huiche.core.api.Get;
import org.huiche.core.api.Save;
import org.huiche.core.entity.BaseEntity;
import org.huiche.core.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 简单Restful风格控制器(非严格遵循规范,可自行根据业务实现编写 BaseController)
 *
 * @author Maning
 */
public class RestCrudApi<T extends BaseEntity> implements Get<T>, Del<T>, Save<T> {
    @Autowired
    protected BaseService<T> service;

    @Override
    public BaseService<T> service() {
        return service;
    }
}
