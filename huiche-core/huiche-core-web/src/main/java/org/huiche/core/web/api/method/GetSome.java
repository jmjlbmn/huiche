package org.huiche.core.web.api.method;

import org.huiche.core.entity.BaseEntity;
import org.huiche.core.exception.Assert;
import org.huiche.core.exception.SystemError;
import org.huiche.core.web.ServiceProvider;
import org.huiche.core.web.api.Api;
import org.huiche.core.web.response.BaseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 获取几条数据
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
        Assert.notNull(SystemError.NOT_NULL, ids);
        return ok(service().get(ids));
    }
}