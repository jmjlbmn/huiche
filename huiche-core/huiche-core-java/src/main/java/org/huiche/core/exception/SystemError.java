package org.huiche.core.exception;

/**
 * @author Maning
 * @version 2017/8/6
 */
public enum SystemError implements BaseError {
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
    NOT_LOGIN(2, "未登录或登录超时"),
    NOT_AUTH(3, "没有权限"),
    NOT_NULL(4, "请求参数不能为空"),
    JSON_ERROR(5, "JSON转换失败"),
    DB_NOT_ALLOW_FIELD__ERROR(6, "字段不符合数据库要求");


    private int id;
    private String msg;

    SystemError(int id, String msg) {
        this.id = id;
        this.msg = msg;
    }

    @Override
    public Integer code() {
        return id;
    }

    @Override
    public String msg() {
        return msg;
    }
}
