package org.huiche.core.web.api.method;

import org.huiche.core.entity.BaseEntity;
import org.huiche.core.exception.Assert;
import org.huiche.core.exception.SystemError;
import org.huiche.core.web.ServiceProvider;
import org.huiche.core.web.api.Api;
import org.huiche.core.web.response.BaseResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Nullable;

/**
 * 删除一条数据
 *
 * @author Maning
 */
public interface DelSome<T extends BaseEntity<T>> extends Api, ServiceProvider<T> {
    /**
     * 删除一条数据
     *
     * @param ids 以逗号分隔的id
     * @return 成功
     */
    @DeleteMapping("some/{ids}")
    default BaseResult<Long> del(@Nullable @PathVariable String ids) {
        Assert.notNull(SystemError.NOT_NULL, ids);
        return ok(service().delete(ids));
    }
}