package org.huiche.core.enums;

import org.huiche.core.consts.If;

/**
 * int是否的枚举,可用于需要传递是否参数的方法,防止出现其他值
 *
 * @author Maning
 */
public enum IfEnum implements ValEnum {
    /**
     * 是
     */
    YES(If.YES),
    /**
     * 否
     */
    NO(If.NO);
    private final int val;


    IfEnum(int val) {
        this.val = val;
    }

    @Override
    public int val() {
        return val;
    }
}
