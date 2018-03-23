package org.huiche.core.util;


import org.huiche.core.exception.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Stack;

/**
 * 数字工具类
 *
 * @author Maning
 */
public class NumberUtil {
    private static final char[] CHARSET_62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

    public static double round(Double d, Integer scale) {
        double num = 0;
        if (null != d) {
            num = d;
        }
        BigDecimal bg = new BigDecimal(num).setScale(scale, RoundingMode.HALF_UP);
        return bg.doubleValue();
    }

    public static double round(BigDecimal d, Integer scale) {
        double num = 0;
        if (null != d) {
            num = d.doubleValue();
        }
        BigDecimal bg = new BigDecimal(num).setScale(scale, RoundingMode.HALF_UP);
        return bg.doubleValue();
    }

    public static double round(BigDecimal d) {
        return round(d, 2);
    }

    public static double round(Double d) {
        return round(d, 2);
    }

    public static long plusPlus(Long num) {
        if (null == num) {
            return 1;
        } else {
            return num + 1;
        }
    }

    public static int plusPlus(Integer num) {
        if (null == num) {
            return 1;
        } else {
            return num + 1;
        }
    }

    public static long minMin(Long num) {
        if (null == num || num < 1) {
            return 0L;
        } else {
            return num - 1;
        }
    }

    public static int minMin(Integer num) {
        if (null == num || num < 1) {
            return 0;
        } else {
            return num - 1;
        }
    }

    public static int add(Integer a, Integer b) {
        int sum = 0;
        if (null != a) {
            sum += a;
        }
        if (null != b) {
            sum += b;
        }
        return sum;
    }

    public static long add(Long a, Long b) {
        long sum = 0;
        if (null != a) {
            sum += a;
        }
        if (null != b) {
            sum += b;
        }
        return sum;
    }

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
