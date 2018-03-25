package org.huiche.core.web.controller;

import org.huiche.core.entity.BaseEntity;
import org.huiche.core.service.BaseCrudService;
import org.huiche.core.web.response.BaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 传统Post风格控制器
 *
 * @author Maning
 */
public class CrudController<T extends BaseEntity> extends BaseController {
    @Autowired
    protected BaseCrudService<T> service;

    /**
     * 获取一条数据
     *
     * @param id id
     * @return 数据
     */
    @PostMapping("get")
    public BaseResult<T> get(Long id) {
        return ok(service.get(id));
    }

    /**
     * 新增或更新,传入id更新,不传入新增
     *
     * @param entity 实体
     * @return ID
     */
    @PostMapping("save")
    public BaseResult<Long> save(T entity) {
        return ok(service.save(entity));
    }

    /**
     * 删除
     *
     * @param id ID
     * @return 成功
     */
    @PostMapping("del")
    public BaseResult del(Long id) {
        service.delete(id);
        return ok();
    }
}
