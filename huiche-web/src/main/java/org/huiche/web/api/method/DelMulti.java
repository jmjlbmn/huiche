package org.huiche.web.api.method;

import org.huiche.core.exception.HuiCheError;
import org.huiche.core.util.Assert;
import org.huiche.core.util.StringUtil;
import org.huiche.data.entity.BaseEntity;
import org.huiche.web.ServiceProvider;
import org.huiche.web.api.Api;
import org.huiche.web.response.BaseResult;
import org.springframework.web.bind.annotation.DeleteMapping;

import javax.annotation.Nullable;

/**
 * restful 删除多条数据
 *
 * @author Maning
 */
public interface DelMulti<T extends BaseEntity<T>> extends Api, ServiceProvider<T> {
    /**
     * 批量删除数据
     *
     * @param ids 以逗号分隔的id
     * @return 删除条数
     */
    @DeleteMapping
    default BaseResult<Long> delMulti(@Nullable String ids) {
        Assert.notNull(HuiCheError.NOT_NULL, ids);
        return ok(service().delete(StringUtil.split2ListLong(ids)));
    }
}