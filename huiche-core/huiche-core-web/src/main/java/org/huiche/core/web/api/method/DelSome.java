package org.huiche.core.web.api.method;

import org.huiche.core.web.api.Api;
import org.huiche.core.web.ServiceProvider;
import org.huiche.core.entity.BaseEntity;
import org.huiche.core.web.response.BaseResult;
import org.springframework.web.bind.annotation.DeleteMapping;

/**
 * @author Maning
 */
public interface DelSome<T extends BaseEntity> extends Api, ServiceProvider<T> {
    /**
     * 删除一条数据
     *
     * @param ids 以逗号分隔的id
     * @return 成功
     */
    @DeleteMapping("some")
    default BaseResult<Long> del(String ids) {
        return ok(service().delete(ids));
    }
}