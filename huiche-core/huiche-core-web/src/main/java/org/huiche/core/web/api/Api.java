package org.huiche.core.web.api;

import org.huiche.core.util.ResultUtil;
import org.huiche.core.web.response.BaseResult;

import javax.annotation.Nonnull;

/**
 * 基础Api
 *
 * @author Maning
 */
public interface Api {
    /**
     * 成功
     *
     * @param j   数据
     * @param <J> 数据类型
     * @return 数据
     */
    default <J> BaseResult<J> ok(@Nonnull J j) {
        return ResultUtil.ok(j);
    }

    /**
     * 成功(无数据)
     *
     * @return 成功
     */
    default BaseResult ok() {
        return ResultUtil.ok();
    }
}
