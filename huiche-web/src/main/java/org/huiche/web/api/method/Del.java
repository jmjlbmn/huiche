package org.huiche.web.api.method;

import org.huiche.data.entity.BaseEntity;
import org.huiche.web.ServiceProvider;
import org.huiche.web.api.Api;
import org.huiche.web.response.BaseResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * restful 删除一条数据
 *
 * @author Maning
 */
public interface Del<T extends BaseEntity<T>> extends Api, ServiceProvider<T> {
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