package org.huiche.web.api;


import org.huiche.web.response.BaseResult;
import org.huiche.web.util.ResultUtil;

import javax.annotation.Nullable;

/**
 * 基础Api控制器,用于封装返回数据为BaseResult
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
    default <J> BaseResult<J> ok(@Nullable J j) {
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
