package org.huiche.core.util;


import org.huiche.core.exception.BaseError;
import org.huiche.core.exception.BaseException;
import org.huiche.core.exception.SystemError;
import org.huiche.core.web.response.BaseResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 响应返回数据结果工具
 *
 * @author Maning
 */
public class ResultUtil {
    /**
     * 成功
     *
     * @return 结果
     */
    public static BaseResult ok() {
        BaseResult baseResult = new BaseResult();
        baseResult.setCode(SystemError.OK.code());
        return baseResult;
    }

    /**
     * 成功
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return 结果
     */
    public static <T> BaseResult<T> ok(@Nonnull T data) {
        BaseResult<T> baseResult = new BaseResult<>();
        baseResult.setCode(SystemError.OK.code());
        baseResult.setData(data);
        return baseResult;
    }

    /**
     * 失败
     *
     * @return 结果
     */
    public static BaseResult fail() {
        return fail(SystemError.FAIL);
    }

    /**
     * 失败
     *
     * @param msg 错误消息
     * @return 结果
     */
    public static BaseResult fail(@Nonnull String msg) {
        return fail(SystemError.FAIL, msg);
    }

    /**
     * 失败
     *
     * @param e 错误对象
     * @return 结果
     */
    public static BaseResult fail(@Nonnull BaseError e) {
        return fail(e.code(), e.msg());
    }

    /**
     * 失败
     *
     * @param e 异常
     * @return 结果
     */
    public static BaseResult fail(@Nonnull BaseException e) {
        return fail(e.getCode(), e.getMsg());
    }

    /**
     * 失败
     *
     * @param code 错误编号
     * @param msg  错误消息
     * @return 结果
     */
    public static BaseResult fail(@Nonnull Integer code, @Nullable String msg) {
        BaseResult baseResult = new BaseResult();
        baseResult.setCode(code);
        baseResult.setMsg(msg);
        return baseResult;
    }

    /**
     * 失败
     *
     * @param e   错误对象
     * @param msg 错误消息
     * @return 结果
     */
    public static BaseResult fail(@Nonnull BaseError e, @Nonnull String msg) {
        return fail(e.code(), msg);
    }
}
