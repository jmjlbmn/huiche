package org.huiche.core.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.huiche.core.exception.BaseError;

/**
 *
 * @author Maning
 * @date 2017/6/24
 * 默认全局返回结果类
 */
@Getter
@Setter
@Accessors(chain = true)
public class BaseResult<T> {
    private Integer code;
    private String msg;
    private T data;

    public BaseResult<T> of(BaseError e) {
        return new BaseResult<T>().setCode(e.code()).setMsg(e.msg());
    }
}
