package org.huiche.core.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 默认异常
 *
 * @author Maning
 */
@Setter
@Getter
public class BaseException extends RuntimeException {
    private Integer code;
    private String msg;


    public BaseException(BaseError e, String msg) {
        super();
        this.code = e.code();
        this.msg = msg;
    }

    public BaseException(BaseError e) {
        super();
        this.msg = e.msg();
        this.code = e.code();
    }

    public static BaseException error(String errorMsg) {
        return new BaseException(SystemError.ERROR, errorMsg);
    }

    public static BaseException fail(String failMsg) {
        return new BaseException(SystemError.FAIL, failMsg);
    }

    @Override
    public String getMessage() {
        return msg + "(" + code + ")";
    }
}
