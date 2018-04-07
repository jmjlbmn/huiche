package org.huiche.core.web.api.method;

import org.huiche.core.entity.BaseEntity;
import org.huiche.core.web.ServiceProvider;
import org.huiche.core.web.api.Api;
import org.huiche.core.web.response.BaseResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 新增
 *
 * @author Maning
 */
public interface Create<T extends BaseEntity> extends Api, ServiceProvider<T> {
    /**
     * 新增
     *
     * @param entity 实体
     * @return ID
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    default BaseResult<Long> createByJson(@RequestBody T entity) {
        return ok(service().create(entity));
    }

    /**
     * 新增
     *
     * @param entity 实体
     * @return ID
     */
    @PostMapping
    default BaseResult<Long> create(T entity) {
        return ok(service().create(entity));
    }
}