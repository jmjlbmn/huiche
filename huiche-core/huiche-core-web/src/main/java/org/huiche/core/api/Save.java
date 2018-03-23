package org.huiche.core.api;

import org.huiche.core.api.base.Api;
import org.huiche.core.api.base.ServiceProvider;
import org.huiche.core.entity.BaseEntity;
import org.huiche.core.response.BaseResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Maning
 */
public interface Save<T extends BaseEntity> extends Api, ServiceProvider<T> {
    /**
     * 新增或更新,传入id更新,不传入新增
     *
     * @param entity 实体
     * @return 变更条数
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    default BaseResult<Long> saveByJson(@RequestBody T entity) {
        return ok(service().save(entity));
    }

    /**
     * 新增或更新,传入id更新,不传入新增
     *
     * @param entity 实体
     * @return 变更条数
     */
    @PostMapping
    default BaseResult<Long> save(T entity) {
        return ok(service().save(entity));
    }
}