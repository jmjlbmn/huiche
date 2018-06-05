package org.huiche.web.api.method;

import org.huiche.data.entity.BaseEntity;
import org.huiche.web.ServiceProvider;
import org.huiche.web.api.Api;
import org.huiche.web.response.BaseResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Nonnull;

/**
 * restful 新增/创建 接口
 *
 * @author Maning
 */
public interface Create<T extends BaseEntity<T>> extends Api, ServiceProvider<T> {
    /**
     * 新增
     *
     * @param entity 实体
     * @return ID
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    default BaseResult<Long> createByJson(@Nonnull @RequestBody T entity) {
        return ok(service().create(entity.setId(null)));
    }

    /**
     * 新增
     *
     * @param entity 实体
     * @return ID
     */
    @PostMapping
    default BaseResult<Long> create(@Nonnull T entity) {
        return ok(service().create(entity.setId(null)));
    }
}