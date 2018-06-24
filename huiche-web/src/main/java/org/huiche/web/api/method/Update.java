package org.huiche.web.api.method;

import org.huiche.data.entity.BaseEntity;
import org.huiche.web.ServiceProvider;
import org.huiche.web.api.Api;
import org.huiche.web.response.BaseResult;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * restful 更新数据接口,部分更新
 *
 * @author Maning
 */
public interface Update<T extends BaseEntity<T>> extends Api, ServiceProvider<T> {
    /**
     * 更新,部分更新
     *
     * @param entity 实体
     * @param id     ID
     * @return ID
     */
    @PatchMapping("{id}")
    default BaseResult<Long> update(@RequestBody T entity, @PathVariable Long id) {
        return ok(service().update(entity.setId(id)));
    }
}