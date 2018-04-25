package org.huiche.core.exception;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
    @Nullable
    private String msg;


    public BaseException(@Nonnull BaseError error, @Nonnull String msg) {
        super();
        this.code = error.code();
        this.msg = msg;
    }

    public BaseException(@Nonnull BaseError error) {
        super();
        this.msg = error.msg();
        this.code = error.code();
    }

    @Nonnull
    public static BaseException error(@Nonnull String msg) {
        return new BaseException(SystemError.ERROR, msg);
    }

    @Nonnull
    public static BaseException fail(@Nonnull String msg) {
        return new BaseException(SystemError.FAIL, msg);
    }

    @Nonnull
    @Override
    public String getMessage() {
        return msg + "(" + code + ")";
    }
}
