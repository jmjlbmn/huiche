package org.huiche.core.util;

import lombok.experimental.UtilityClass;
import org.huiche.core.exception.Assert;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 * 验证码工具类
 *
 * @author Maning
 */
@UtilityClass
public class CaptchaUtil {
    /**
     * 获取六位随机验证码
     *
     * @return 随机验证码
     */
    @Nonnull
    public static String getNumber() {
        return getNumber(6);
    }

    /**
     * 生成一个随机数字验证码,长度小于16位
     *
     * @param size 长度
     * @return 验证码
     */
    @Nonnull
    public static String getNumber(int size) {
        Assert.ok("长度应在1-16位之间", size > 0 && size <= 16);
        String result = "000000000000000" + new Random().nextInt((int) Math.pow(10, size));
        return result.substring(result.length() - size);
    }
}
