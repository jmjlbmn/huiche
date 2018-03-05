package org.huiche.core.consts;

import org.huiche.core.annotation.ConstField;

/**
 * 是否,用于所有判断是否的情况
 *
 * @author Maning
 */
public interface If extends ConstClass {
    @ConstField("是")
    int YES = 1;
    @ConstField("否")
    int NO = 0;

    /**
     * 布尔转Integer,默认null
     *
     * @param flag 是否
     * @return Integer 是否
     */
    static Integer of(Boolean flag) {
        if (null == flag) {
            return null;
        }
        if (flag) {
            return YES;
        } else {
            return NO;
        }
    }

    /**
     * 布尔转Integer,默认No (0)
     *
     * @param flag 是否
     * @return Integer 是否
     */
    static Integer ofDefaultNo(Boolean flag) {
        if (null == flag) {
            return NO;
        }
        if (flag) {
            return YES;
        } else {
            return NO;
        }
    }

    /**
     * Integer 转布尔,默认null
     *
     * @param flag 是否
     * @return 布尔是否
     */
    static Boolean to(Integer flag) {
        if (YES == flag) {
            return true;
        }
        if (NO == flag) {
            return false;
        }
        return null;
    }

    /**
     * Integer 转布尔,默认false
     *
     * @param flag 是否
     * @return 布尔是否
     */
    static Boolean toDefaultFalse(Integer flag) {
        return YES == flag;
    }
}
