package org.huiche.core.util;

import org.huiche.core.consts.Const;
import org.huiche.core.exception.BaseException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * 字符工具类
 *
 * @author Maning
 * @version 2017/8/6
 */
public class StringUtil {

    public static boolean isEmpty(String str) {
        return BaseUtil.isEmpty(str);
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static String toCamel(String str) {
        StringBuilder result = new StringBuilder();
        if (str == null || str.isEmpty()) {
            return "";
        } else if (!str.contains(Const.UNDERLINE)) {
            return str.substring(0, 1).toLowerCase() + str.substring(1);
        } else {
            String[] columns = str.split(Const.UNDERLINE);
            for (String columnSplit : columns) {
                if (columnSplit.isEmpty()) {
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

    public static String convertFristToUpperCase(String temp) {
        String first = temp.substring(0, 1);
        String other = temp.substring(1);
        return first.toUpperCase(Locale.getDefault()) + other;
    }

    public static String hideIdNumber(String idNumber) {
        StringBuilder sb = new StringBuilder("");
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

    public static String hideRealName(String realName) {
        StringBuilder sb = new StringBuilder("");
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

    public static String hideCardNumber(String cardNumber) {
        int showSize = 4;
        int allSize = 16;
        StringBuilder sb = new StringBuilder("");
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

    public static String hidePhone(String phone) {
        int length = 11;
        StringBuilder sb = new StringBuilder("");
        if (StringUtil.isNotEmpty(phone)) {
            phone = phone.trim();
            if (CheckUtil.isPhoneNumber(phone) || phone.length() == length) {
                sb.append(phone.substring(0, 3));
                sb.append("****");
                sb.append(phone.substring(7, 11));
            } else {
                sb.append(phone);
            }
        }
        return sb.toString();
    }

    public static String join(String[] arr) {
        return join(arr, Const.COMMA);
    }

    public static String join(String[] arr, String sep) {
        StringBuilder sb = new StringBuilder();
        if (null != arr && arr.length > 0) {
            for (String s : arr) {
                sb.append(sep).append(s);
            }
        }
        return sb.toString().replaceFirst(sep, "");
    }

    public static String join(Collection<String> list) {
        return join(list, Const.COMMA);
    }

    public static String join(Collection<String> list, String sep) {
        StringBuilder sb = new StringBuilder();
        if (null != list && list.size() > 0) {
            for (String s : list) {
                sb.append(sep).append(s);
            }
        }
        return sb.toString().replaceFirst(sep, "");
    }

    public static String[] list2Arr(List<String> list) {
        if (BaseUtil.isNotEmpty(list)) {
            return list.toArray(new String[list.size()]);
        }
        return new String[0];
    }

    public static String fromCamel(String camelStr) {
        if (camelStr == null || camelStr.isEmpty()) {
            return "";
        }
        StringBuilder column = new StringBuilder();
        column.append(camelStr.substring(0, 1).toLowerCase());
        for (int i = 1; i < camelStr.length(); i++) {
            String s = camelStr.substring(i, i + 1);
            if (!Character.isDigit(s.charAt(0)) && s.equals(s.toUpperCase())) {
                column.append(Const.UNDERLINE);
            }
            column.append(s.toLowerCase());
        }
        return column.toString();
    }

    public static int getCharLength(String str) {
        return getCharLength(str, null);
    }

    public static int getCharLength(String str, String charset) {
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
            throw BaseException.error("传入编码类型 '" + charset + "' 有误!");
        }
    }

    public static List<String> split2List(String str) {
        List<String> list = new ArrayList<>();
        if (isNotEmpty(str)) {
            String[] arr = str.split(Const.COMMA);
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

    public static List<Integer> split2ListInt(String str) {
        List<Integer> list = new ArrayList<>();
        try {
            if (isNotEmpty(str)) {
                String[] arr = str.split(Const.COMMA);
                if (arr.length > 0) {
                    for (String s : arr) {
                        if (isNotEmpty(s)) {
                            list.add(Integer.parseInt(s));
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw BaseException.error("类型转换错误,无法转换成 int 类型");
        }
        return list;
    }
    public static List<Long> split2ListLong(String str) {
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
            throw BaseException.error("类型转换错误,无法转换成 long 类型");
        }
        return list;
    }
}
