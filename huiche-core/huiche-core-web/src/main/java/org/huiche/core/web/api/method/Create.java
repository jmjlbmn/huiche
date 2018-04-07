package org.huiche.core.web.api.method;

import org.huiche.core.entity.BaseEntity;
import org.huiche.core.exception.Assert;
import org.huiche.core.exception.SystemError;
import org.huiche.core.web.ServiceProvider;
import org.huiche.core.web.api.Api;
import org.huiche.core.web.response.BaseResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Nonnull;

/**
 * 新增
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
        Assert.isNull(SystemError.CREATE_CAN_NOT_HAS_ID, entity.getId());
        return ok(service().create(entity));
    }

    /**
     * 新增
     *
     * @param entity 实体
     * @return ID
     */
    @PostMapping
    default BaseResult<Long> create(@Nonnull T entity) {
        Assert.isNull(SystemError.CREATE_CAN_NOT_HAS_ID, entity.getId());
        return ok(service().create(entity));
    }
}