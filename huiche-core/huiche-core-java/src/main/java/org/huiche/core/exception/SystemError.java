package org.huiche.core.exception;

/**
 * 系统内置基本异常
 *
 * @author Maning
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
    CREATE_CAN_NOT_HAS_ID(2, "创建时不允许有ID"),
    UPDATE_MUST_HAVA_ID(3, "更新数据时,实体必须设置ID"),
    NOT_NULL(4, "请求参数不能为空"),
    JSON_ERROR(5, "JSON转换失败"),
    DB_NOT_ALLOW_FIELD_ERROR(6, "字段不符合数据库要求"),
    NO_EXISTS(7, "指定对象不存在");


    private final int id;
    private final String msg;

    SystemError(int id, String msg) {
        this.id = id;
        this.msg = msg;
    }

    @Override
    public int code() {
        return id;
    }

    @Override
    public String msg() {
        return msg;
    }
}
