package org.huiche.core.api;

import org.huiche.core.api.base.Api;
import org.huiche.core.api.base.ServiceProvider;
import org.huiche.core.entity.BaseEntity;
import org.huiche.core.response.BaseResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Maning
 */
public interface Del<T extends BaseEntity> extends Api, ServiceProvider<T> {
    /**
     * 删除一条数据
     *
     * @param id ID
     * @return 成功
     */
    @DeleteMapping("{id}")
    default BaseResult<Long> del(@PathVariable Long id) {
        return ok(service().delete(id));
    }
}