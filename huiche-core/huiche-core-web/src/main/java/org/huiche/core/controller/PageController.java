package org.huiche.core.controller;

import org.huiche.core.page.PageRequest;
import org.huiche.core.page.PageResponse;
import org.huiche.core.response.BaseResult;

/**
 * @author Maning
 * @version 2017/8/8
 */
public interface PageController<T> {
    /**
     * 分页获取数据
     *
     * @param pageRequest 分页请求
     * @param search      搜索条件
     * @return 数据
     */
    BaseResult<PageResponse<T>> page(PageRequest pageRequest, T search);
}
