package org.huiche.core.util;


import lombok.experimental.UtilityClass;
import org.huiche.core.exception.Assert;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Stack;

/**
 * 数字工具类
 *
 * @author Maning
 */
@UtilityClass
public class NumberUtil {
    private static final char[] CHARSET_62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

    /**
     * 四舍五入
     *
     * @param d     数字
     * @param scale 保留几位小数
     * @return 四舍五入后的数字
     */
    public static double round(@Nullable Double d, int scale) {
        double num = 0;
        if (null != d) {
            num = d;
        }
        BigDecimal bg = new BigDecimal(num).setScale(scale, RoundingMode.HALF_UP);
        return bg.doubleValue();
    }

    /**
     * 四舍五入
     *
     * @param d     数字
     * @param scale 保留几位小数
     * @return 四舍五入后的数字
     */
    public static double round(@Nullable BigDecimal d, int scale) {
        double num = 0;
        if (null != d) {
            num = d.doubleValue();
        }
        BigDecimal bg = new BigDecimal(num).setScale(scale, RoundingMode.HALF_UP);
        return bg.doubleValue();
    }

    /**
     * 四舍五入保留2位小数
     *
     * @param d 数字
     * @return 四舍五入后的数字
     */
    public static double round(@Nullable BigDecimal d) {
        return round(d, 2);
    }

    /**
     * 四舍五入保留2位小数
     *
     * @param d 数字
     * @return 四舍五入后的数字
     */

    public static double round(@Nullable Double d) {
        return round(d, 2);
    }

    /**
     * 包装类++操作
     *
     * @param num 数值
     * @return 增加后的值
     */
    public static long plusPlus(@Nullable Long num) {
        if (null == num) {
            return 1;
        } else {
            return num + 1;
        }
    }

    /**
     * 包装类++操作
     *
     * @param num 数值
     * @return 增加后的值
     */
    public static int plusPlus(@Nullable Integer num) {
        if (null == num) {
            return 1;
        } else {
            return num + 1;
        }
    }

    /**
     * 包装类--操作
     *
     * @param num 数值
     * @return 减小后的值
     */
    public static long minMin(@Nullable Long num) {
        if (null == num || num < 1) {
            return 0L;
        } else {
            return num - 1;
        }
    }

    /**
     * 包装类--操作
     *
     * @param num 数值
     * @return 减小后的值
     */
    public static int minMin(@Nullable Integer num) {
        if (null == num || num < 1) {
            return 0;
        } else {
            return num - 1;
        }
    }

    /**
     * 包装类相加操作
     *
     * @param a 对象a
     * @param b 对象b
     * @return a+b
     */
    public static int add(@Nullable Integer a, @Nullable Integer b) {
        int sum = 0;
        if (null != a) {
            sum += a;
        }
        if (null != b) {
            sum += b;
        }
        return sum;
    }

    /**
     * 包装类相加操作
     *
     * @param a 对象a
     * @param b 对象b
     * @return a+b
     */
    public static long add(@Nullable Long a, @Nullable Long b) {
        long sum = 0;
        if (null != a) {
            sum += a;
        }
        if (null != b) {
            sum += b;
        }
        return sum;
    }

    /**
     * 转62进制
     *
     * @param number 数字
     * @return 62进制数字
     */
    @Nonnull
    public static String turn62(long number) {
        return turn62(number, false);
    }

    /**
     * 转换62进制
     *
     * @param number 要转换的数字
     * @param fill   是否补齐11位
     * @return 转换后的62进制数字
     */
    @Nonnull
    public static String turn62(long number, boolean fill) {
        Assert.ok("暂不支持负数", number >= 0);
        Long rest = number;
        Stack<Character> stack = new Stack<>();
        StringBuilder result = fill ? new StringBuilder("0000000000") : new StringBuilder(11);
        if (number == 0) {
            result.append("0");
        } else {
            while (rest != 0) {
                stack.add(CHARSET_62[new Long((rest - (rest / 62) * 62)).intValue()]);
                rest = rest / 62;
            }
            for (; !stack.isEmpty(); ) {
                result.append(stack.pop());
            }
        }
        return fill ? result.substring(result.length() - 11) : result.toString();
    }
}
