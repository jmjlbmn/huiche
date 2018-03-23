package org.huiche.core.util;

import lombok.experimental.UtilityClass;
import org.huiche.core.exception.Assert;

import java.util.Random;

/**
 * 验证码工具类
 *
 * @author Maning
 */
@UtilityClass
public class CaptchaUtil {
    public static String getNumber() {
        return getNumber(6);
    }

    /**
     * 生成一个随机数字验证码,长度小于16位
     *
     * @param size 长度
     * @return 验证码
     */
    public static String getNumber(int size) {
        Assert.ok("长度应在1-16位之间", size > 0 && size <= 16);
        String result = "000000000000000" + new Random().nextInt((int) Math.pow(10, size));
        return result.substring(result.length() - size);
    }
}
