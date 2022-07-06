package org.huiche.core.exception;

/**
 * 系统内置基本异常
 *
 * @author Maning
 */
public enum HuiCheError implements BaseError {
    /**
     * 系统错误(系统原因错误或用户原因但未捕获的错误)
     */
    ERROR(-1, "系统错误"),
    /**
     * 操作失败(用户原因造成错误导致)
     */
    FAIL(1, "操作失败");


    private final int id;
    private final String message;

    HuiCheError(int id, String message) {
        this.id = id;
        this.message = message;
    }

    @Override
    public int code() {
        return id;
    }

    @Override
    public String msg() {
        return message;
    }
}
