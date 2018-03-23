package org.huiche.core.api.base;

import org.huiche.core.response.BaseResult;
import org.huiche.core.util.ResultUtil;

/**
 * @author Maning
 */
public interface Api {
    default <J> BaseResult<J> ok(J j) {
        return ResultUtil.ok(j);
    }

    default BaseResult ok() {
        return ResultUtil.ok();
    }
}
