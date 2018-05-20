package org.huiche.web.controller;

import org.huiche.data.entity.BaseEntity;
import org.huiche.data.page.PageRequest;
import org.huiche.data.page.PageResponse;
import org.huiche.web.response.BaseResult;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Nonnull;

/**
 * 传统Post风格增删改查分页控制器
 *
 * @author Maning
 */
public abstract class BaseCrudPageController<T extends BaseEntity<T>> extends BaseCrudController<T> {
    /**
     * 分页获取数据
     *
     * @param pageRequest 分页请求
     * @param search      筛选
     * @return 数据
     */
    @PostMapping("page")
    public BaseResult<PageResponse<T>> page(@Nonnull PageRequest pageRequest, @Nonnull T search) {
        return ok(service().page(pageRequest, search));
    }
}
