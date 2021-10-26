package org.huiche.web.response;

import org.huiche.core.exception.BaseError;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * 默认全局返回结果类
 *
 * @author Maning
 */
public class BaseResult<T> implements Serializable {
    private Integer code;
    private String msg;
    private T data;

    public BaseResult(String msg) {
        this.msg = msg;
    }

    public BaseResult() {
    }

    public static BaseResult of(@Nonnull BaseError e) {
        return new BaseResult().setCode(e.code()).setMsg(e.msg());
    }

    public Integer getCode() {
        return code;
    }

    public BaseResult<T> setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public BaseResult<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public BaseResult<T> setData(T data) {
        this.data = data;
        return this;
    }
}
