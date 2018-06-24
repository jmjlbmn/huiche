package org.huiche.web.api.method;

import org.huiche.data.entity.BaseEntity;
import org.huiche.web.ServiceProvider;
import org.huiche.web.api.Api;
import org.huiche.web.response.BaseResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * restful 新增或更新,传入id更新,不传入新增
 *
 * @author Maning
 */
public interface Save<T extends BaseEntity<T>> extends Api, ServiceProvider<T> {
    /**
     * 新增或更新,传入id更新,不传入新增
     *
     * @param entity 实体
     * @return 变更条数
     */
    @PostMapping
    default BaseResult<Long> save(@RequestBody T entity) {
        return ok(service().save(entity));
    }
}