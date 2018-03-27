package org.huiche.core.exception;

/**
 * 异常错误接口,可以通过实现此类(建议使用枚举),来定义异常,另外请code值在1000以上,因为1000以下系统默认留给http状态码使用
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