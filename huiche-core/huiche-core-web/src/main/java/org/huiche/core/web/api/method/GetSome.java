package org.huiche.core.web.api.method;

import org.huiche.core.entity.BaseEntity;
import org.huiche.core.web.ServiceProvider;
import org.huiche.core.web.api.Api;
import org.huiche.core.web.response.BaseResult;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 获取几条数据
 *
 * @author Maning
 */
public interface GetSome<T extends BaseEntity> extends Api, ServiceProvider<T> {
    /**
     * 获取几条数据
     *
     * @param ids 逗号分隔的
     * @return 数据
     */
    @GetMapping("some")
    default BaseResult<List<T>> get(String ids) {
        return ok(service().get(ids));
    }
}