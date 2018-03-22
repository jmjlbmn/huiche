package org.huiche.core.api;

import org.huiche.core.entity.BaseEntity;
import org.huiche.core.response.BaseResult;
import org.huiche.core.util.ResultUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Maning
 */
public interface Create<T extends BaseEntity> extends Api<T> {
    /**
     * 新增
     *
     * @param entity 实体
     * @return ID
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    default BaseResult<Long> createByJson(@RequestBody T entity) {
        return ResultUtil.ok(service().create(entity));
    }

    /**
     * 新增
     *
     * @param entity 实体
     * @return ID
     */
    @PostMapping
    default BaseResult<Long> create(T entity) {
        return ResultUtil.ok(service().create(entity));
    }
}