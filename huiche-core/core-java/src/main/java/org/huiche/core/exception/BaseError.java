package org.huiche.core.exception;

/**
 * 异常错误
 *
 * @author Maning
 */
public interface BaseError {
    /**
     * 错误代码
     *
     * @return 错误代码
     */
    Integer code();

    /**
     * 错误描述
     *
     * @return 错误描述
     */
    String msg();
}