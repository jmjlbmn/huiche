package org.huiche.core.api;

import org.huiche.core.entity.BaseEntity;
import org.huiche.core.response.BaseResult;
import org.huiche.core.util.ResultUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Maning
 */
public interface GetOne<T extends BaseEntity> extends Api<T> {
    /**
     * 获取单条数据
     *
     * @param id id
     * @return 数据
     */
    @GetMapping("{id}")
    default BaseResult<T> getOne(@PathVariable Long id) {
        return ResultUtil.ok(service().get(id));
    }
}