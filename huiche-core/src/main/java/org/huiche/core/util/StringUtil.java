package org.huiche.core.util;

import org.huiche.core.exception.HuiCheException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * 字符串处理工具类,很常用
 *
 * @author Maning
 */
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
                column.append("_");
            }
            column.append(s.toLowerCase());
        }
        return column.toString();
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
     * 逗号分隔的字符串
     *
     * @param str 逗号分隔的long
     * @return list
     */
    @Nonnull
    public static List<Long> split2ListLong(@Nullable String str) {
        return split2ListLong(str, ",");
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
                String[] arr = str.split(sep);
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

}
