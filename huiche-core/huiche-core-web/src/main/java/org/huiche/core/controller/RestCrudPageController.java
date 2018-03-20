package org.huiche.core.controller;

import org.huiche.core.entity.BaseEntity;
import org.huiche.core.page.PageRequest;
import org.huiche.core.page.PageResponse;
import org.huiche.core.response.BaseResult;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 简单Restful风格控制器(非严格遵循规范,可自行根据业务实现编写BaseController)
 *
 * @author Maning
 */
public class RestCrudPageController<T extends BaseEntity> extends RestCrudController<T> {
    @GetMapping
    public BaseResult<PageResponse<T>> page(PageRequest pageRequest, T search) {
        return json(service.page(pageRequest, search));
    }
}
