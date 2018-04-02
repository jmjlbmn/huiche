package org.huiche.core.web.controller;

import org.huiche.core.entity.BaseEntity;
import org.huiche.core.page.PageRequest;
import org.huiche.core.page.PageResponse;
import org.huiche.core.web.response.BaseResult;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 传统Post风格增删改查分页控制器
 *
 * @author Maning
 */
public abstract class BaseCrudPageController<T extends BaseEntity> extends BaseCrudController<T> {
    /**
     * 分页获取数据
     *
     * @param pageRequest 分页请求
     * @param search      筛选
     * @return 数据
     */
    @PostMapping("page")
    public BaseResult<PageResponse<T>> page(PageRequest pageRequest, T search) {
        return ok(service().page(pageRequest, search));
    }
}
