package org.huiche.web.api.method;

import org.huiche.data.entity.BaseEntity;
import org.huiche.data.page.PageRequest;
import org.huiche.data.page.PageResponse;
import org.huiche.web.ServiceProvider;
import org.huiche.web.api.Api;
import org.huiche.web.response.BaseResult;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * restful 分页获取数据
 *
 * @author Maning
 */
public interface Page<T extends BaseEntity<T>> extends Api, ServiceProvider<T> {
    /**
     * 分页获取数据
     *
     * @param pageRequest 分页
     * @param search      简单筛选
     * @return 数据
     */
    @GetMapping
    default BaseResult<PageResponse<T>> page(PageRequest pageRequest, T search) {
        return ok(service().page(pageRequest, search));
    }
}