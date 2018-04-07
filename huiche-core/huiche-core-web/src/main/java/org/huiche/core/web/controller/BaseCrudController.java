package org.huiche.core.web.controller;

import org.huiche.core.entity.BaseEntity;
import org.huiche.core.exception.Assert;
import org.huiche.core.exception.SystemError;
import org.huiche.core.web.ServiceProvider;
import org.huiche.core.web.response.BaseResult;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 传统Post风格增删改查控制器
 *
 * @author Maning
 */
public abstract class BaseCrudController<T extends BaseEntity<T>> extends BaseController implements ServiceProvider<T> {

    /**
     * 获取一条数据
     *
     * @param id id
     * @return 数据
     */
    @PostMapping("get")
    public BaseResult<T> get(@Nullable Long id) {
        Assert.notNull(SystemError.NOT_NULL, id);
        return ok(service().get(id));
    }

    /**
     * 新增或更新,传入id更新,不传入新增
     *
     * @param entity 实体
     * @return ID
     */
    @PostMapping("save")
    public BaseResult<Long> save(@Nonnull T entity) {
        return ok(service().save(entity));
    }

    /**
     * 删除
     *
     * @param id ID
     * @return 成功
     */
    @PostMapping("del")
    public BaseResult del(@Nullable Long id) {
        Assert.notNull(SystemError.NOT_NULL, id);
        service().delete(id);
        return ok();
    }
}
