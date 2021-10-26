package org.huiche.web;

import java.io.Serializable;

/**
 * @author Maning
 */
public class ErrorVO implements Serializable {
    private Integer errCode;
    private String errMsg;

    public Integer getErrCode() {
        return errCode;
    }

    public ErrorVO setErrCode(Integer errCode) {
        this.errCode = errCode;
        return this;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public ErrorVO setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }

    @Override
    public String toString() {
        return "ErrorVO{" +
                "errCode=" + errCode +
                ", errMsg='" + errMsg + '\'' +
                '}';
    }
}
