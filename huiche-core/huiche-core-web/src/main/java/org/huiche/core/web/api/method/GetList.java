package org.huiche.core.web.api.method;

import org.huiche.core.entity.BaseEntity;
import org.huiche.core.web.ServiceProvider;
import org.huiche.core.web.api.Api;
import org.huiche.core.web.response.BaseResult;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * 获取数据集合
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