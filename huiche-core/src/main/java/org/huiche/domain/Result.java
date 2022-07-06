package org.huiche.domain;

import java.io.Serializable;

/**
 * @author Maning
 */

public class Result implements Serializable {
    private String code;
    private String msg;
    private Object payload;

    public static Result of(Object payload) {
        Result result = new Result();
        result.payload = payload;
        result.code = "00000";
        result.msg = "success";
        return result;
    }

    public static Result ok() {
        Result result = new Result();
        result.code = "00000";
        result.msg = "success";
        return result;
    }

    public static Result of(String code, String msg) {
        Result result = new Result();
        result.code = code;
        result.msg = msg;
        return result;
    }

    public static Result fail(String msg) {
        Result result = new Result();
        result.code = "A0001";
        result.msg = msg;
        return result;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }
}
