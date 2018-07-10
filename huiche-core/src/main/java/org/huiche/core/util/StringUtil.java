package org.huiche.core.util;

import lombok.experimental.UtilityClass;
import org.huiche.core.consts.Const;
import org.huiche.core.exception.HuiCheException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * 字符串处理工具类,很常用
 *
 * @author Maning
 */
@UtilityClass
public class StringUtil {
    /**
     * 是否null或空字符串
     *
     * @param str 字符串
     * @return 是否
     */
    public static boolean isEmpty(@Nullable String str) {
        return HuiCheUtil.isEmpty(str);
    }

    /**
     * 是否不是null或空字符串
     *
     * @param str 字符串
     * @return 是否
     */
    public static boolean isNotEmpty(@Nullable String str) {
        return !isEmpty(str);
    }

    /**
     * 属性名转数据库字段名
     *
     * @param str 属性名
     * @return 字段名
     */
    @Nonnull
    public static String fromDb(@Nonnull String str) {
        StringBuilder result = new StringBuilder();
        if (!str.contains(Const.UNDERLINE)) {
            return str.substring(0, 1).toLowerCase() + str.substring(1);
        } else {
            String[] columns = str.split(Const.UNDERLINE);
            for (String columnSplit : columns) {
                if (columnSplit.length() == 0) {
                    continue;
                }
                if (result.length() == 0) {
                    result.append(columnSplit.toLowerCase());
                } else {
                    result.append(columnSplit.substring(0, 1).toUpperCase()).append(columnSplit.substring(1).toLowerCase());
                }
            }
            return result.toString();
        }

    }

    /**
     * 数据库字段名转属性名
     *
     * @param dbStr 数据库字段名
     * @return 属性名
     */
    @Nonnull
    public static String toDb(@Nonnull String dbStr) {
        StringBuilder column = new StringBuilder();
        column.append(dbStr.substring(0, 1).toLowerCase());
        for (int i = 1; i < dbStr.length(); i++) {
            String s = dbStr.substring(i, i + 1);
            if (!Character.isDigit(s.charAt(0)) && s.equals(s.toUpperCase())) {
                column.append(Const.UNDERLINE);
            }
            column.append(s.toLowerCase());
        }
        return column.toString();
    }

    /**
     * get方法名转数据库字段名
     *
     * @param methodName 方法名
     * @return 字段名
     */
    @Nonnull
    public static String getMethodName2FieldName(@Nonnull String methodName) {
        return toDb(methodName.substring(3));
    }

    /**
     * 字符串首字母大写
     *
     * @param str 要转换的字符串
     * @return 转换后的字符串
     */
    @Nonnull
    public static String convertFirstToUpperCase(@Nonnull String str) {
        String first = str.substring(0, 1);
        String other = str.substring(1);
        return first.toUpperCase(Locale.getDefault()) + other;
    }

    /**
     * 身份证号码部分隐藏
     *
     * @param idNumber 身份证号码
     * @return 处理后的身份证号码
     */
    @Nonnull
    public static String hideIdNumber(@Nonnull String idNumber) {
        StringBuilder sb = new StringBuilder();
        if (StringUtil.isNotEmpty(idNumber)) {
            idNumber = idNumber.trim();
            String[] arr = idNumber.split("");
            if (arr.length > 0) {
                sb.append(arr[0]);
                sb.append("****************");
                if (arr.length > 1) {
                    sb.append(arr[arr.length - 1]);
                } else {
                    sb.append("*");
                }
            }
        }
        return sb.toString();
    }

    /**
     * 隐藏真实姓名(只显示姓)
     *
     * @param realName 姓名
     * @return 处理后的姓名
     */
    @Nonnull
    public static String hideRealName(@Nonnull String realName) {
        StringBuilder sb = new StringBuilder();
        if (StringUtil.isNotEmpty(realName)) {
            realName = realName.trim();
            String[] arr = realName.split("");
            if (arr.length > 0) {
                sb.append(arr[0]);
                sb.append("**");
            }
        }
        return sb.toString();
    }

    /**
     * 隐藏部分银行卡号
     *
     * @param cardNumber 银行卡号
     * @return 处理后的银行卡号
     */
    @Nonnull
    public static String hideCardNumber(@Nonnull String cardNumber) {
        int showSize = 4;
        int allSize = 16;
        StringBuilder sb = new StringBuilder();
        if (StringUtil.isNotEmpty(cardNumber)) {
            cardNumber = cardNumber.trim();
            String[] arr = cardNumber.split("");
            int length = arr.length;
            if (length > showSize) {
                cardNumber = cardNumber.substring(length - showSize);
            }
            cardNumber = "****************" + cardNumber;
            sb.append(cardNumber.substring(cardNumber.length() - allSize));
        }
        return sb.toString();
    }

    /**
     * 隐藏手机号中间四位
     *
     * @param phone 手机号
     * @return 处理后的手机号
     */
    @Nonnull
    public static String hidePhone(@Nonnull String phone) {
        int length = 11;
        StringBuilder sb = new StringBuilder();
        if (StringUtil.isNotEmpty(phone)) {
            phone = phone.trim();
            if (CheckUtil.isPhoneNumber(phone) || phone.length() == length) {
                sb.append(phone, 0, 3);
                sb.append("****");
                sb.append(phone, 7, 11);
            } else {
                sb.append(phone);
            }
        }
        return sb.toString();
    }

    /**
     * 字符串数组用逗号分隔拼接的字符串
     *
     * @param arr 字符串数组
     * @return 拼接和的字符串
     */
    @Nonnull
    public static String join(@Nonnull String... arr) {
        return join(arr, Const.COMMA);
    }

    /**
     * 字符串数组用分隔符拼接的字符串
     *
     * @param arr 字符串数组
     * @param sep 分隔符
     * @return 拼接和的字符串
     */
    @Nonnull
    public static String join(@Nonnull String[] arr, @Nonnull String sep) {
        StringBuilder sb = new StringBuilder();
        if (arr.length > 0) {
            for (Object s : arr) {
                if (null != s) {
                    sb.append(sep).append(s);
                }
            }
        }
        return sb.toString().replaceFirst(sep, "");
    }

    /**
     * 字符串集合用逗号分隔拼接的字符串
     *
     * @param list 字符串集合
     * @return 拼接和的字符串
     */
    @Nonnull
    public static String join(@Nonnull Collection<?> list) {
        return join(list, Const.COMMA);
    }

    /**
     * 字符串集合用分隔符拼接的字符串
     *
     * @param list 字符串集合
     * @param sep  分隔符
     * @return 拼接和的字符串
     */
    @Nonnull
    public static String join(@Nonnull Collection<?> list, @Nonnull String sep) {
        StringBuilder sb = new StringBuilder();
        if (list.size() > 0) {
            for (Object s : list) {
                if (null != s) {
                    sb.append(sep).append(s.toString());
                }
            }
        }
        return sb.toString().replaceFirst(sep, "");
    }

    /**
     * 字符串集合转字符串数组
     *
     * @param list 集合
     * @return 数组
     */
    @Nonnull
    public static String[] list2Arr(@Nonnull Collection<String> list) {
        if (HuiCheUtil.isNotEmpty(list)) {
            return list.toArray(new String[0]);
        }
        return new String[0];
    }

    /**
     * 获取字符串的字节长度,默认UTF-8编码
     *
     * @param str 字符串
     * @return 长度
     */
    public static int getCharLength(@Nonnull String str) {
        return getCharLength(str, null);
    }

    /**
     * 获取字符串的字节长度
     *
     * @param str     字符串
     * @param charset 字符串编码
     * @return 长度
     */
    public static int getCharLength(@Nonnull String str, @Nullable String charset) {
        if (isEmpty(str)) {
            return 0;
        }
        if (isEmpty(charset)) {
            charset = "UTF-8";
        }
        try {
            return str.getBytes(charset).length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new HuiCheException("传入编码类型 '" + charset + "' 有误!");
        }
    }

    /**
     * 逗号分隔的字符串
     *
     * @param str 逗号分隔的字符串
     * @return 字符串list
     */
    @Nonnull
    public static List<String> split2List(@Nullable String str) {
        return split2List(str, Const.COMMA);
    }

    /**
     * 逗号分隔的字符串
     *
     * @param str 分隔的字符串
     * @param sep 分隔符号
     * @return 字符串list
     */
    @Nonnull
    public static List<String> split2List(@Nullable String str, @Nonnull String sep) {
        if (HuiCheUtil.isEmpty(str)) {
            return Collections.emptyList();
        }
        List<String> list = new ArrayList<>();
        if (isNotEmpty(str)) {
            String[] arr = str.split(sep);
            if (arr.length > 0) {
                for (String s : arr) {
                    if (isNotEmpty(s)) {
                        list.add(s);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 逗号分隔的字符串
     *
     * @param str 逗号分隔的int
     * @return list
     */
    @Nonnull
    public static List<Integer> split2ListInt(@Nullable String str) {
        return split2ListInt(str, Const.COMMA);
    }

    /**
     * 逗号分隔的字符串
     *
     * @param str 分隔的int
     * @param sep 分隔符号
     * @return list
     */
    @Nonnull
    public static List<Integer> split2ListInt(@Nullable String str, @Nonnull String sep) {
        if (HuiCheUtil.isEmpty(str)) {
            return Collections.emptyList();
        }
        List<Integer> list = new ArrayList<>();
        try {
            if (isNotEmpty(str)) {
                String[] arr = str.split(sep);
                if (arr.length > 0) {
                    for (String s : arr) {
                        if (isNotEmpty(s)) {
                            list.add(Integer.parseInt(s));
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new HuiCheException("类型转换错误,无法转换成 int 类型");
        }
        return list;
    }

    /**
     * 逗号分隔的字符串
     *
     * @param str 逗号分隔的long
     * @return list
     */
    @Nonnull
    public static List<Long> split2ListLong(@Nullable String str) {
        return split2ListLong(str, Const.COMMA);
    }

    /**
     * 逗号分隔的字符串
     *
     * @param str 分隔的long
     * @param sep 分隔符号
     * @return list
     */
    @Nonnull
    public static List<Long> split2ListLong(@Nullable String str, @Nonnull String sep) {
        if (HuiCheUtil.isEmpty(str)) {
            return Collections.emptyList();
        }
        List<Long> list = new ArrayList<>();
        try {
            if (isNotEmpty(str)) {
                String[] arr = str.split(Const.COMMA);
                if (arr.length > 0) {
                    for (String s : arr) {
                        if (isNotEmpty(s)) {
                            list.add(Long.parseLong(s));
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new HuiCheException("类型转换错误,无法转换成 long 类型");
        }
        return list;
    }

    /**
     * 生成一个随机数字验证码,长度小于16位
     *
     * @param size 长度
     * @return 验证码
     */
    @Nonnull
    public static String randomNumber(int size) {
        Assert.ok("长度应在1-16位之间", size > 0 && size <= 16);
        String result = "000000000000000" + new Random().nextInt((int) Math.pow(10, size));
        return result.substring(result.length() - size);
    }
}
