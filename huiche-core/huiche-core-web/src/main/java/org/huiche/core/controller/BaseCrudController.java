package org.huiche.core.controller;


import org.huiche.core.response.BaseResult;

/**
 * @author Maning
 * @version 2017/8/8
 */
public abstract class BaseCrudController<T> extends BaseController {
    /**
     * 获取单条记录
     *
     * @param id ID
     * @return 记录
     */
    public abstract BaseResult<T> get(Long id);

    /**
     * 保存单条记录
     *
     * @param t 记录
     * @return 记录ID
     */
    public abstract BaseResult<Long> save(T t);

    /**
     * 删除单条记录
     *
     * @param id ID
     * @return 成功/失败
     */
    public abstract BaseResult delete(Long id);
}
