package org.huiche.core.web.api.method;

import org.huiche.core.entity.BaseEntity;
import org.huiche.core.exception.Assert;
import org.huiche.core.util.BaseUtil;
import org.huiche.core.web.ServiceProvider;
import org.huiche.core.web.api.Api;
import org.huiche.core.web.response.BaseResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 更新
 *
 * @author Maning
 */
public interface Update<T extends BaseEntity<T>> extends Api, ServiceProvider<T> {
    /**
     * 更新
     *
     * @param entity 实体
     * @param id     ID
     * @return ID
     */
    @PutMapping("{id}")
    default BaseResult<Long> update(@Nonnull @RequestBody T entity, @Nullable @PathVariable Long id) {
        Long eId = entity.getId();
        Assert.ok("要更新的对象ID不一致", null == eId || BaseUtil.equals(eId, id));
        return ok(service().update(entity));
    }
}