package org.huiche.core.exception;

import lombok.Getter;
import lombok.Setter;
import org.huiche.core.util.StringUtil;

/**
 * 内置基础异常
 *
 * @author Maning
 */
@Setter
@Getter
public class HuiCheException extends RuntimeException {
    /**
     * 错误代码
     */
    private int code;
    /**
     * 错误信息描述
     */
    private String msg;

    public HuiCheException() {
        super(HuiCheError.ERROR.msg());
        this.msg = HuiCheError.ERROR.msg();
        this.code = HuiCheError.ERROR.code();
    }

    public HuiCheException(String msg) {
        super(StringUtil.isEmpty(msg) ? HuiCheError.FAIL.msg() : msg);
        this.code = HuiCheError.FAIL.code();
        this.msg = getMessage();
    }

    public HuiCheException(Throwable e) {
        super(e);
        this.msg = HuiCheError.ERROR.msg();
        this.code = HuiCheError.ERROR.code();
    }

    public HuiCheException(BaseError error) {
        super(null == error ? HuiCheError.ERROR.msg() : error.msg());
        this.code = null == error ? HuiCheError.ERROR.code() : error.code();
        this.msg = getMessage();
    }

    public HuiCheException(String msg, Throwable e) {
        super(StringUtil.isEmpty(msg) ? HuiCheError.FAIL.msg() : msg, e);
        this.code = HuiCheError.FAIL.code();
        this.msg = getMessage();
    }

    public HuiCheException(BaseError error, String msg) {
        super(StringUtil.isEmpty(msg) ? HuiCheError.FAIL.msg() : msg);
        this.code = null == error ? HuiCheError.FAIL.code() : error.code();
        this.msg = getMessage();
    }
}
