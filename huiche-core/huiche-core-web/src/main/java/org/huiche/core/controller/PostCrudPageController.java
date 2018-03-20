package org.huiche.core.controller;

import org.huiche.core.entity.BaseEntity;
import org.huiche.core.page.PageRequest;
import org.huiche.core.page.PageResponse;
import org.huiche.core.response.BaseResult;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 传统Post风格控制器
 *
 * @author Maning
 */
public class PostCrudPageController<T extends BaseEntity> extends PostCrudController<T> {
    @PostMapping("page")
    public BaseResult<PageResponse<T>> page(PageRequest pageRequest, T search) {
        return json(service.page(pageRequest, search));
    }
}
