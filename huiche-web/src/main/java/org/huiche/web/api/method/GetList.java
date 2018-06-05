package org.huiche.web.api.method;

import org.huiche.data.entity.BaseEntity;
import org.huiche.web.ServiceProvider;
import org.huiche.web.api.Api;
import org.huiche.web.response.BaseResult;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * restful 获取数据集合
 *
 * @author Maning
 */
public interface GetList<T extends BaseEntity<T>> extends Api, ServiceProvider<T> {
    /**
     * 获取数据集合
     *
     * @param search 简单筛选
     * @return ID
     */
    @GetMapping("list")
    default BaseResult<List<T>> list(@Nonnull T search) {
        return ok(service().list(search));
    }
}