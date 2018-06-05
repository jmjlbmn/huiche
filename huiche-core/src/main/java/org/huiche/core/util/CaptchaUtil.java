package org.huiche.core.util;

import lombok.experimental.UtilityClass;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 * 验证码工具类
 *
 * @author Maning
 */
@UtilityClass
public final class CaptchaUtil {
    @Nonnull
    public static String getNumber() {
        return getNumber(6);
    }

    @Nonnull
    public static String getNumber(int size) {
        Assert.ok("长度应在1-16位之间", size > 0 && size <= 16);
        String result = "000000000000000" + (new Random()).nextInt((int) Math.pow(10.0D, (double) size));
        return result.substring(result.length() - size);
    }
}