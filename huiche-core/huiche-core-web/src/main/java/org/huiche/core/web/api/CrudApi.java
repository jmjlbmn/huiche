package org.huiche.core.web.api;

import org.huiche.core.entity.BaseEntity;
import org.huiche.core.service.BaseCrudService;
import org.huiche.core.web.api.method.Del;
import org.huiche.core.web.api.method.Get;
import org.huiche.core.web.api.method.Page;
import org.huiche.core.web.api.method.Save;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 简单Restful风格控制器(非严格遵循规范,可自行根据业务实现编写 BaseController)
 *
 * @author Maning
 */
public class CrudApi<T extends BaseEntity> implements Get<T>, Del<T>, Save<T>, Page<T> {
    @Autowired
    protected BaseCrudService<T> service;

    @Override
    public BaseCrudService<T> service() {
        return service;
    }
}
