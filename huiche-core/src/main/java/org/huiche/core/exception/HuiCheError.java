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
     * 成功
     */
    OK(0, "成功"),
    /**
     * 操作失败(用户原因造成错误导致)
     */
    FAIL(1, "操作失败"),
    /**
     * 参数不能为空
     */
    NOT_NULL(2, "参数不能为空"),
    /**
     * 数据不存在
     */
    NO_EXISTS(3, "数据不存在"),
    /**
     * 更新数据必须设置ID
     */
    UPDATE_MUST_HAVE_ID(4, "更新数据必须设置ID");


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
