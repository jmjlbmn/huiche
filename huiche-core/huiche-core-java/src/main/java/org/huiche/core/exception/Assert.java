package org.huiche.core.exception;


import org.huiche.core.util.BaseUtil;

/**
 * @author Maning
 * @version 2017/7/11
 */
public class Assert {
    /**
     * 判断条件,false 抛出
     *
     * @param e  异常
     * @param ok 判断条件
     */
    public static void ok(BaseError e, boolean ok) {
        if (!ok) {
            throw new BaseException(e);
        }
    }

    /**
     * 判断条件,false 抛出
     *
     * @param failMsg 错误说明
     * @param ok      判断条件
     */
    public static void ok(String failMsg, boolean ok) {
        if (!ok) {
            throw BaseException.fail(failMsg);
        }
    }

    /**
     * 判断是否相等,不相等抛出
     *
     * @param e 异常
     * @param a 对象a
     * @param b 对象b
     */
    public static void equals(BaseError e, Object a, Object b) {
        if (!BaseUtil.equals(a, b)) {
            throw new BaseException(e);
        }
    }

    /**
     * 判断是否相等,不相等抛出
     *
     * @param failMsg 错误说明
     * @param a       对象a
     * @param b       对象b
     */
    public static void equals(String failMsg, Object a, Object b) {
        if (!BaseUtil.equals(a, b)) {
            throw BaseException.fail(failMsg);
        }
    }

    /**
     * 判断是否不相等,相等抛出
     *
     * @param e 异常
     * @param a 对象a
     * @param b 对象b
     */
    public static void noEquals(BaseError e, Object a, Object b) {
        if (BaseUtil.equals(a, b)) {
            throw new BaseException(e);
        }
    }

    /**
     * 判断是否不相等,相等抛出
     *
     * @param failMsg 错误说明
     * @param a       对象a
     * @param b       对象b
     */
    public static void noEquals(String failMsg, Object a, Object b) {
        if (BaseUtil.equals(a, b)) {
            throw BaseException.fail(failMsg);
        }
    }

    /**
     * 判断是否是null或空,如果不是抛出
     *
     * @param e   异常
     * @param obj 对象
     */
    public static void isNull(BaseError e, Object obj) {
        if (BaseUtil.isNotEmpty(obj)) {
            throw new BaseException(e);
        }
    }

    /**
     * 判断是否是null或空,如果不是抛出
     *
     * @param e   异常
     * @param obj 对象
     */
    public static void isNull(BaseError e, Object... obj) {
        isNull(e.msg(), obj);
    }

    /**
     * 判断是否是null或空,如果不是抛出
     *
     * @param failMsg 错误说明
     * @param obj     对象
     */
    public static void isNull(String failMsg, Object... obj) {
        if (null != obj && obj.length > 0) {
            for (Object o : obj) {
                if (BaseUtil.isNotEmpty(o)) {
                    throw BaseException.fail(failMsg);
                }
            }
        }
    }

    /**
     * 判断是否不是空或null,是空抛出
     *
     * @param e   异常
     * @param obj 对象
     */
    public static void notNull(BaseError e, Object obj) {
        if (BaseUtil.isEmpty(obj)) {
            throw new BaseException(e);
        }
    }

    /**
     * 判断是否不是空或null,是空抛出
     *
     * @param e   异常
     * @param obj 对象
     */
    public static void notNull(BaseError e, Object... obj) {
        if (null != obj && obj.length > 0) {
            for (Object o : obj) {
                if (BaseUtil.isEmpty(o)) {
                    throw new BaseException(e);
                }
            }
        }
    }

    /**
     * 判断是否不是空或null,是空抛出
     *
     * @param obj 对象
     */
    public static void notNull(Object... obj) {
        if (null != obj && obj.length > 0) {
            for (Object o : obj) {
                if (BaseUtil.isEmpty(o)) {
                    throw new BaseException(SystemError.NOT_NULL);
                }
            }
        }
    }

    /**
     * 判断是否不是空或null,是空抛出
     *
     * @param failMsg 错误说明
     * @param obj     对象
     */
    public static void notNull(String failMsg, Object... obj) {
        if (null != obj && obj.length > 0) {
            for (Object o : obj) {
                if (BaseUtil.isEmpty(o)) {
                    throw BaseException.fail(failMsg);
                }
            }
        }
    }

    /**
     * 判断不全是null或空,如果全是空,抛出
     *
     * @param e   异常
     * @param obj 对象
     */
    public static void notAllNull(BaseError e, Object... obj) {
        ok(e, isAllNull(obj));
    }

    /**
     * 判断不全是null或空,如果全是空,抛出
     *
     * @param failMsg 错误说明
     * @param obj     对象
     */
    public static void notAllNull(String failMsg, Object... obj) {
        ok(failMsg, isAllNull(obj));
    }

    /**
     * 如果不是空,则只需后续OK判断,如果OK判断false抛出
     *
     * @param e      异常
     * @param testOk 后续判断
     * @param obj    对象
     */
    public static void ifNotNull(BaseError e, Ok testOk, Object... obj) {
        if (isNotNull(obj)) {
            if (!testOk.ok()) {
                throw new BaseException(e);
            }
        }
    }

    /**
     * 如果不是空,则只需后续OK判断,如果OK判断false抛出
     *
     * @param failMsg 错误说明
     * @param testOk  后续判断
     * @param obj     对象
     */
    public static void ifNotNull(String failMsg, Ok testOk, Object... obj) {
        if (isNotNull(obj)) {
            if (!testOk.ok()) {
                throw BaseException.fail(failMsg);
            }
        }
    }

    /**
     * 先执行testOk判断,如果为true,则只需ifOk判断,如果false抛出
     *
     * @param e      异常
     * @param ifOK   异常判断
     * @param testOk 是否执行判断
     */
    public static void ifTest(BaseError e, Ok ifOK, Ok testOk) {
        if (ifOK.ok()) {
            ok(e, testOk.ok());
        }
    }

    /**
     * 先执行testOk判断,如果为true,则只需ifOk判断,如果false抛出
     *
     * @param failMsg 错误说明
     * @param ifOK    异常判断
     * @param testOk  是否执行判断
     */
    public static void ifTest(String failMsg, Ok ifOK, Ok testOk) {
        if (ifOK.ok()) {
            ok(failMsg, testOk.ok());
        }
    }

    private static boolean isAllNull(Object... obj) {
        if (null != obj && obj.length > 0) {
            for (Object o : obj) {
                if (BaseUtil.isNotEmpty(o)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private static boolean isNotNull(Object... obj) {
        if (null != obj && obj.length > 0) {
            for (Object o : obj) {
                if (BaseUtil.isEmpty(o)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 提供一个对错是否
     *
     * @see java.util.function.Supplier
     */
    @FunctionalInterface
    public interface Ok {
        /**
         * 判断条件
         *
         * @return 是否
         */
        boolean ok();
    }
}
