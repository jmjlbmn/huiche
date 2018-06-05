package org.huiche.web.util;


import org.huiche.core.exception.BaseError;
import org.huiche.core.exception.HuiCheError;
import org.huiche.core.exception.HuiCheException;
import org.huiche.web.response.BaseResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 响应返回数据结果工具,用于封装数据为BaseResult
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
        baseResult.setCode(HuiCheError.OK.code());
        return baseResult;
    }

    /**
     * 成功
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return 结果
     */
    public static <T> BaseResult<T> ok(@Nullable T data) {
        BaseResult<T> baseResult = new BaseResult<>();
        baseResult.setCode(HuiCheError.OK.code());
        baseResult.setData(data);
        return baseResult;
    }

    /**
     * 失败
     *
     * @return 结果
     */
    public static BaseResult fail() {
        return fail(HuiCheError.FAIL);
    }

    /**
     * 失败
     *
     * @param msg 错误消息
     * @return 结果
     */
    public static BaseResult fail(@Nonnull String msg) {
        return fail(HuiCheError.FAIL, msg);
    }

    /**
     * 失败
     *
     * @param error 错误对象
     * @return 结果
     */
    public static BaseResult fail(@Nonnull BaseError error) {
        return fail(error.code(), error.msg());
    }

    /**
     * 失败
     *
     * @param e 异常
     * @return 结果
     */
    public static BaseResult fail(@Nonnull HuiCheException e) {
        return fail(e.getCode(), e.getMsg());
    }

    /**
     * 失败
     *
     * @param code 错误编号
     * @param msg  错误消息
     * @return 结果
     */
    public static BaseResult fail(int code, @Nullable String msg) {
        BaseResult baseResult = new BaseResult();
        baseResult.setCode(code);
        baseResult.setMsg(null == msg ? HuiCheError.FAIL.msg() : msg);
        return baseResult;
    }

    /**
     * 失败
     *
     * @param error 错误对象
     * @param msg   错误消息
     * @return 结果
     */
    public static BaseResult fail(@Nonnull BaseError error, @Nullable String msg) {
        return fail(error.code(), msg);
    }
}
