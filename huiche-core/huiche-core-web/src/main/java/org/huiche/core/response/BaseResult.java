package org.huiche.core.response;

import org.huiche.core.exception.BaseError;

/**
 *
 * @author Maning
 * 默认全局返回结果类
 */
public class BaseResult<T> {
    private Integer code;
    private String msg;
    private T data;

    public BaseResult<T> of(BaseError e) {
        return new BaseResult<T>().setCode(e.code()).setMsg(e.msg());
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

    @Override
    public String toString() {
        return "BaseResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
