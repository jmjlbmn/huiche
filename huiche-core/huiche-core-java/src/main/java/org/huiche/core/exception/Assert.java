package org.huiche.core.exception;


import org.huiche.core.util.BaseUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 断言工具
 *
 * @author Maning
 */
public class Assert {
    /**
     * 判断条件,false 抛出
     *
     * @param e  异常
     * @param ok 判断条件
     */
    public static void ok(@Nonnull BaseError e, boolean ok) {
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
    public static void ok(@Nonnull String failMsg, boolean ok) {
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
    public static void equals(@Nonnull BaseError e, @Nullable Object a, @Nullable Object b) {
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
    public static void equals(@Nonnull String failMsg, @Nullable Object a, @Nullable Object b) {
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
    public static void noEquals(@Nonnull BaseError e, @Nullable Object a, @Nullable Object b) {
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
    public static void noEquals(@Nonnull String failMsg, @Nullable Object a, @Nullable Object b) {
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
    public static void isNull(@Nonnull BaseError e, @Nullable Object obj) {
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
    public static void isNull(@Nonnull BaseError e, @Nonnull Object... obj) {
        if (obj.length > 0) {
            for (Object o : obj) {
                if (BaseUtil.isNotEmpty(o)) {
                    throw new BaseException(e);
                }
            }
        }
    }

    /**
     * 判断是否是null或空,如果不是抛出
     *
     * @param failMsg 错误说明
     * @param obj     对象
     */
    public static void isNullWithMsg(@Nonnull String failMsg, @Nonnull Object... obj) {
        if (obj.length > 0) {
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
    public static void notNull(@Nonnull BaseError e, @Nullable Object obj) {
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
    public static void notNull(@Nonnull BaseError e, @Nonnull Object... obj) {
        if (obj.length > 0) {
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
    public static void notNull(@Nonnull Object... obj) {
        if (obj.length > 0) {
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
    public static void notNullWithMsg(@Nonnull String failMsg, @Nonnull Object... obj) {
        if (obj.length > 0) {
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
    public static void notAllNull(@Nonnull BaseError e, @Nonnull Object... obj) {
        ok(e, isAllNull(obj));
    }

    /**
     * 判断不全是null或空,如果全是空,抛出
     *
     * @param failMsg 错误说明
     * @param obj     对象
     */
    public static void notAllNull(@Nonnull String failMsg, @Nonnull Object... obj) {
        ok(failMsg, isAllNull(obj));
    }

    /**
     * 如果不是空,则只需后续OK判断,如果OK判断false抛出
     *
     * @param e      异常
     * @param testOk 后续判断
     * @param obj    对象
     */
    public static void ifNotNull(@Nonnull BaseError e, @Nonnull Ok testOk, @Nonnull Object... obj) {
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
    public static void ifNotNull(@Nonnull String failMsg, @Nonnull Ok testOk, @Nonnull Object... obj) {
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
    public static void ifTest(@Nonnull BaseError e, @Nonnull Ok ifOK, @Nonnull Ok testOk) {
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
    public static void ifTest(@Nonnull String failMsg, @Nonnull Ok ifOK, @Nonnull Ok testOk) {
        if (ifOK.ok()) {
            ok(failMsg, testOk.ok());
        }
    }

    private static boolean isAllNull(@Nonnull Object... obj) {
        if (obj.length > 0) {
            for (Object o : obj) {
                if (BaseUtil.isNotEmpty(o)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private static boolean isNotNull(@Nonnull Object... obj) {
        if (obj.length > 0) {
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
