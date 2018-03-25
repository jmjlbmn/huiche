package org.huiche.core.web.controller;

import org.huiche.core.entity.BaseEntity;
import org.huiche.core.page.PageRequest;
import org.huiche.core.page.PageResponse;
import org.huiche.core.web.response.BaseResult;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 传统Post风格控制器
 *
 * @author Maning
 */
public class CrudPageController<T extends BaseEntity> extends CrudController<T> {
    @PostMapping("page")
    public BaseResult<PageResponse<T>> page(PageRequest pageRequest, T search) {
        return ok(service.page(pageRequest, search));
    }
}
