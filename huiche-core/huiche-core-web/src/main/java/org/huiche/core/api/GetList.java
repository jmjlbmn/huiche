package org.huiche.core.api;

import org.huiche.core.api.base.Api;
import org.huiche.core.api.base.ServiceProvider;
import org.huiche.core.entity.BaseEntity;
import org.huiche.core.response.BaseResult;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author Maning
 */
public interface GetList<T extends BaseEntity> extends Api, ServiceProvider<T> {
    /**
     * 获取数据集合
     *
     * @param search 简单筛选
     * @return ID
     */
    @GetMapping("list")
    default BaseResult<List<T>> list(T search) {
        return ok(service().list(search));
    }
}