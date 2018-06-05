package org.huiche.web.api.method;

import org.huiche.core.exception.HuiCheError;
import org.huiche.core.util.Assert;
import org.huiche.data.entity.BaseEntity;
import org.huiche.web.ServiceProvider;
import org.huiche.web.api.Api;
import org.huiche.web.response.BaseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Nullable;

/**
 * restful 获取单条数据
 *
 * @author Maning
 */
public interface Get<T extends BaseEntity<T>> extends Api, ServiceProvider<T> {
    /**
     * 获取单条数据
     *
     * @param id id
     * @return 数据
     */
    @GetMapping("{id}")
    default BaseResult<T> get(@Nullable @PathVariable Long id) {
        Assert.notNull(HuiCheError.NOT_NULL, id);
        return ok(service().get(id));
    }
}