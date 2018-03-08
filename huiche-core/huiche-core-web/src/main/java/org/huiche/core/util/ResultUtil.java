package org.huiche.core.util;


import org.huiche.core.exception.BaseError;
import org.huiche.core.exception.SystemError;
import org.huiche.core.response.BaseResult;

/**
 * 响应返回数据结果工具
 *
 * @author Maning
 * @version 1.0
 */
public class ResultUtil {
    public static BaseResult ok() {
        BaseResult baseResult = new BaseResult();
        baseResult.setCode(SystemError.OK.code());
        return baseResult;
    }

    public static <T> BaseResult<T> ok(T data) {
        BaseResult<T> baseResult = new BaseResult<>();
        baseResult.setCode(SystemError.OK.code());
        baseResult.setData(data);
        return baseResult;
    }

    public static BaseResult fail() {
        return fail(SystemError.FAIL);
    }

    public static BaseResult fail(BaseError e) {
        return fail(e.code(), e.msg());
    }

    public static BaseResult fail(Integer code, String msg) {
        BaseResult baseResult = new BaseResult();
        baseResult.setCode(code);
        baseResult.setMsg(msg);
        return baseResult;
    }

    public static BaseResult fail(BaseError e, String msg) {
        return fail(e.code(), msg);
    }
}
