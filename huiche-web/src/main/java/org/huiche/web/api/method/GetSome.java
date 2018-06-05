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
import java.util.List;

/**
 * restful 获取几条数据
 *
 * @author Maning
 */
public interface GetSome<T extends BaseEntity<T>> extends Api, ServiceProvider<T> {
    /**
     * 获取几条数据
     *
     * @param ids 逗号分隔的
     * @return 数据
     */
    @GetMapping("some/{ids}")
    default BaseResult<List<T>> get(@Nullable @PathVariable String ids) {
        Assert.notNull(HuiCheError.NOT_NULL, ids);
        return ok(service().get(ids));
    }
}