package org.huiche.core.api;

import org.huiche.core.entity.BaseEntity;
import org.huiche.core.page.PageRequest;
import org.huiche.core.page.PageResponse;
import org.huiche.core.response.BaseResult;
import org.huiche.core.util.ResultUtil;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Maning
 */
public interface Page<T extends BaseEntity> extends Api<T> {
    /**
     * 分页获取数据
     *
     * @param pageRequest 分页
     * @param search      简单筛选
     * @return 数据
     */
    @GetMapping
    default BaseResult<PageResponse<T>> page(PageRequest pageRequest, T search) {
        return ResultUtil.ok(service().page(pageRequest, search));
    }
}