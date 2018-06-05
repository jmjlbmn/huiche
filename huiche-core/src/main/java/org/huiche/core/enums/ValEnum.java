package org.huiche.core.enums;

/**
 * int值的枚举,通过实现此接口来使用相关方法,常用于标识状态/类型等字段,进行可控值范围的参数传递
 *
 * @author Maning
 */
public interface ValEnum {
    /**
     * 枚举对应的值
     *
     * @return 枚举对应的值
     */
    int val();
}
