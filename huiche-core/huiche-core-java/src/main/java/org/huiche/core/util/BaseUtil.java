package org.huiche.core.util;

import lombok.experimental.UtilityClass;
import org.huiche.core.consts.Const;
import org.huiche.core.exception.Assert;
import org.huiche.core.exception.SystemError;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * 基础工具类
 *
 * @author Maning
 */
@UtilityClass
public class BaseUtil {
    /**
     * 是否是空集合或数组
     *
     * @param o 集合/数组
     * @return 是否空
     */
    public static boolean isListOrArray(Object o) {
        Assert.ok(SystemError.NOT_NULL, null != o);
        return o instanceof Iterable || o.getClass().isArray();
    }

    /**
     * 是否不是空集合/数组
     *
     * @param o 集合/数组
     * @return 是否不是空
     */
    public static boolean isNotListAndArray(Object o) {
        return !isListOrArray(o);
    }

    /**
     * 是否是空对象/字符串/集合/数组等等
     *
     * @param obj 对象
     * @return 是否空
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        } else {
            if (obj instanceof CharSequence) {
                return isEmpty((CharSequence) obj);
            } else if (obj instanceof Collection) {
                return ((Collection) obj).isEmpty();
            } else if (obj instanceof Map) {
                return ((Map) obj).isEmpty();
            } else {
                return isEmpty(obj.toString());
            }
        }
    }

    /**
     * 是否是空字符
     *
     * @param obj 字符对象
     * @return 是否是空
     */
    public static boolean isEmpty(CharSequence obj) {
        if (obj == null) {
            return true;
        } else {
            if (obj.length() == 0) {
                return true;
            }
            String objStr = obj.toString().trim();
            while (objStr.startsWith(Const.QUOTE_DOUBLE) && objStr.endsWith(Const.QUOTE_DOUBLE) || objStr.startsWith(Const.QUOTE_SINGLE) && objStr.endsWith(Const.QUOTE_SINGLE)) {
                objStr = objStr.substring(1, objStr.length() - 1).trim();
            }
            return objStr.length() == 0 || "null".equalsIgnoreCase(objStr) || "undefined".equalsIgnoreCase(objStr);
        }
    }

    /**
     * 是否是空集合
     *
     * @param obj 集合
     * @return 是否空
     */
    public static boolean isEmpty(Collection obj) {
        return obj == null || obj.isEmpty();
    }

    /**
     * 是否是空map
     *
     * @param obj map
     * @return 是否空
     */
    public static boolean isEmpty(Map obj) {
        return obj == null || obj.isEmpty();
    }

    /**
     * 判断是否全是空,只要一个非空返回false
     *
     * @param obj 对象
     * @return 是否全是空
     */
    public static boolean isEmpty(Object... obj) {
        for (Object o : obj) {
            if (isNotEmpty(o)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否不是空对象
     *
     * @param obj 对象
     * @return 是否不是空
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     * 是否不是空字符
     *
     * @param obj 字符
     * @return 是否不是空
     */
    public static boolean isNotEmpty(CharSequence obj) {
        return !isEmpty(obj);
    }

    /**
     * 是否不是空集合
     *
     * @param obj 集合对象
     * @return 是否不是空
     */
    public static boolean isNotEmpty(Collection obj) {
        return !isEmpty(obj);
    }

    /**
     * 是否不是空map
     *
     * @param obj map
     * @return 是否不是空
     */
    public static boolean isNotEmpty(Map obj) {
        return !isEmpty(obj);
    }

    /**
     * 判断是否全是非空,只要又一个空,返回false
     *
     * @param obj 对象
     * @return 是否全是非空
     */
    public static boolean isNotEmpty(Object... obj) {
        for (Object o : obj) {
            if (isEmpty(o)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否相等比较
     *
     * @param a   对象a
     * @param b   对象b
     * @param <T> 对象类型
     * @return 是否相等
     */
    public static <T> boolean equals(T a, T b) {
        return Objects.equals(a, b);
    }

    /**
     * 是否不相等比较
     *
     * @param a   对象a
     * @param b   对象b
     * @param <T> 对象类型
     * @return 是否不相等
     */
    public static <T> boolean noEquals(T a, T b) {
        return !equals(a, b);
    }

    /**
     * 对象是否在对象数组中
     *
     * @param src    对象
     * @param target 对象数组
     * @return 是否在
     */
    public static boolean in(Object src, Object... target) {
        if (null != target) {
            for (Object t : target) {
                if (Objects.equals(src, t)) {
                    return true;
                }
            }
            return false;
        }
        return Objects.equals(src, null);
    }

    /**
     * 如果是空返回null
     *
     * @param t   对象
     * @param <T> 对象类型
     * @return 转换后对象
     */
    public static <T> T empty2Null(T t) {
        return isEmpty(t) ? null : t;
    }

}
