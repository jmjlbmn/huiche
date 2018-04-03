package org.huiche.core.web.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.huiche.core.exception.BaseError;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * 默认全局返回结果类
 *
 * @author Maning
 */
@Setter
@Getter
@Accessors(chain = true)
@ToString
public class BaseResult<T> implements Serializable {
    private Integer code;
    private String msg;
    private T data;

    public BaseResult<T> of(@Nonnull BaseError e) {
        return new BaseResult<T>().setCode(e.code()).setMsg(e.msg());
    }

}
