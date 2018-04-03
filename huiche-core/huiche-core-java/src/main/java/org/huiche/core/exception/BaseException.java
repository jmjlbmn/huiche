package org.huiche.core.exception;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;

/**
 * 基础异常
 *
 * @author Maning
 */
@Setter
@Getter
public class BaseException extends RuntimeException {
    /**
     * 错误代码
     */
    private int code;
    /**
     * 错误信息描述
     */
    private String msg;


    public BaseException(@Nonnull BaseError e, @Nonnull String msg) {
        super();
        this.code = e.code();
        this.msg = msg;
    }

    public BaseException(@Nonnull BaseError e) {
        super();
        this.msg = e.msg();
        this.code = e.code();
    }

    @Nonnull
    public static BaseException error(@Nonnull String errorMsg) {
        return new BaseException(SystemError.ERROR, errorMsg);
    }

    @Nonnull
    public static BaseException fail(@Nonnull String failMsg) {
        return new BaseException(SystemError.FAIL, failMsg);
    }

    @Override
    public String getMessage() {
        return msg + "(" + code + ")";
    }
}
