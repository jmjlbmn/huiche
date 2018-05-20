package org.huiche.core.consts;

import org.huiche.annotation.consts.ConstVal;
import org.huiche.core.util.HuiCheUtil;

import javax.annotation.Nullable;

/**
 * 是否,用于所有判断是否的情况
 *
 * @author Maning
 */
public interface If {
    @ConstVal("是")
    int YES = 1;
    @ConstVal("否")
    int NO = 0;

    /**
     * 布尔转Integer,默认No (0)
     *
     * @param flag 是否
     * @return Integer 是否
     */
    static int of(@Nullable Boolean flag) {
        if (HuiCheUtil.equals(true, flag)) {
            return YES;
        } else {
            return NO;
        }
    }

    /**
     * Integer 转布尔,默认false
     *
     * @param flag 是否
     * @return 布尔是否
     */
    static boolean to(@Nullable Integer flag) {
        return HuiCheUtil.equals(YES, flag);
    }
}
