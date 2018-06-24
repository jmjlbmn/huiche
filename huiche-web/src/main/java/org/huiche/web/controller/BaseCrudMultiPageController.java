package org.huiche.web.controller;

import org.huiche.core.exception.HuiCheError;
import org.huiche.core.util.Assert;
import org.huiche.data.entity.BaseEntity;
import org.huiche.web.response.BaseResult;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Nullable;

/**
 * 传统Post风格增删改查分页控制器
 *
 * @author Maning
 */
public abstract class BaseCrudMultiPageController<T extends BaseEntity<T>> extends BaseCrudPageController<T> {
    /**
     * 分页获取数据
     *
     * @param ids 以逗号分隔的id
     * @return 删除条数
     */
    @PostMapping("delMulti")
    public BaseResult<Long> delMulti(@Nullable String ids) {
        Assert.notNull(HuiCheError.NOT_NULL, ids);
        return ok(service().delete(ids));
    }
}
