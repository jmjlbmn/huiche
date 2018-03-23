package org.huiche.core.util;

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
public class BaseUtil {
    public static boolean isListOrArray(Object o) {
        Assert.ok(SystemError.NOT_NULL, null != o);
        return o instanceof Iterable || o.getClass().isArray();
    }

    public static boolean isNotListAndArray(Object o) {
        return !isListOrArray(o);
    }

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

    public static boolean isEmpty(Collection obj) {
        return obj == null || obj.isEmpty();
    }

    public static boolean isEmpty(Map obj) {
        return obj == null || obj.isEmpty();
    }

    /**
     * 判断是否全是空,只要一个非空返回false
     */
    public static boolean isEmpty(Object... obj) {
        for (Object o : obj) {
            if (isNotEmpty(o)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public static boolean isNotEmpty(CharSequence obj) {
        return !isEmpty(obj);
    }

    public static boolean isNotEmpty(Collection obj) {
        return !isEmpty(obj);
    }

    public static boolean isNotEmpty(Map obj) {
        return !isEmpty(obj);
    }

    /**
     * 判断是否全是非空,只要又一个空,返回false
     */
    public static boolean isNotEmpty(Object... obj) {
        for (Object o : obj) {
            if (isEmpty(o)) {
                return false;
            }
        }
        return true;
    }

    public static Object empty2Null(Object obj) {
        return isEmpty(obj) ? null : obj;
    }

    public static <T> boolean equals(T a, T b) {
        return Objects.equals(a, b);
    }

    public static <T> boolean noEquals(T a, T b) {
        return !equals(a, b);
    }

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

    public static <T> T if2Null(T t) {
        return isEmpty(t) ? null : t;
    }

}
