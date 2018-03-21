package org.huiche.core.controller;

import org.huiche.core.api.Api;
import org.huiche.core.api.DelOne;
import org.huiche.core.api.GetOne;
import org.huiche.core.entity.BaseEntity;
import org.huiche.core.response.BaseResult;
import org.huiche.core.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 简单Restful风格控制器(非严格遵循规范,可自行根据业务实现编写BaseController)
 *
 * @author Maning
 */
public class RestCrudController<T extends BaseEntity> extends BaseController implements Api<T>, GetOne<T>, DelOne<T> {
    @Autowired
    protected BaseService<T> service;

    /**
     * 新增或更新,传入id更新,不传入新增
     *
     * @param entity 实体
     * @return ID
     */
    @PostMapping
    public BaseResult<Long> save(@RequestBody T entity) {
        return json(service.save(entity));
    }

    @Override
    public BaseService<T> service() {
        return service;
    }
}
