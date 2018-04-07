package org.huiche.core.exception;

import org.huiche.core.util.BaseUtil;
import org.jetbrains.annotations.Contract;

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
     * @param error 异常
     * @param ok    判断条件
     */
    @Contract(value = "_,false->fail", pure = true)
    public static void ok(@Nonnull BaseError error, boolean ok) {
        if (!ok) {
            throw new BaseException(error);
        }
    }

    /**
     * 判断条件,false 抛出
     *
     * @param msg 错误说明
     * @param ok  判断条件
     */
    @Contract(value = "_,false->fail", pure = true)
    public static void ok(@Nonnull String msg, boolean ok) {
        if (!ok) {
            throw BaseException.fail(msg);
        }
    }

    /**
     * 判断是否相等,不相等抛出
     *
     * @param error 异常
     * @param a     对象a
     * @param b     对象b
     */
    public static void equals(@Nonnull BaseError error, @Nullable Object a, @Nullable Object b) {
        if (!BaseUtil.equals(a, b)) {
            throw new BaseException(error);
        }
    }

    /**
     * 判断是否相等,不相等抛出
     *
     * @param msg 错误说明
     * @param a   对象a
     * @param b   对象b
     */
    public static void equals(@Nonnull String msg, @Nullable Object a, @Nullable Object b) {
        if (!BaseUtil.equals(a, b)) {
            throw BaseException.fail(msg);
        }
    }

    /**
     * 判断是否不相等,相等抛出
     *
     * @param error 异常
     * @param a     对象a
     * @param b     对象b
     */
    public static void noEquals(@Nonnull BaseError error, @Nullable Object a, @Nullable Object b) {
        if (BaseUtil.equals(a, b)) {
            throw new BaseException(error);
        }
    }

    /**
     * 判断是否不相等,相等抛出
     *
     * @param msg 错误说明
     * @param a   对象a
     * @param b   对象b
     */
    public static void noEquals(@Nonnull String msg, @Nullable Object a, @Nullable Object b) {
        if (BaseUtil.equals(a, b)) {
            throw BaseException.fail(msg);
        }
    }

    /**
     * 判断是否是null或空,如果不是抛出
     *
     * @param error 异常
     * @param obj   对象
     */
    @Contract(value = "_,!null->fail", pure = true)
    public static void isNull(@Nonnull BaseError error, @Nullable Object obj) {
        if (BaseUtil.isNotEmpty(obj)) {
            throw new BaseException(error);
        }
    }

    /**
     * 判断是否是null或空,如果不是抛出
     *
     * @param msg 错误说明
     * @param obj 对象
     */
    @Contract(value = "_,!null->fail", pure = true)
    public static void isNull(@Nonnull String msg, @Nullable Object obj) {
        if (BaseUtil.isNotEmpty(obj)) {
            throw BaseException.fail(msg);
        }
    }

    /**
     * 判断是否是null或空,如果不是抛出
     *
     * @param error 异常
     * @param obj   对象
     */
    public static void isNull(@Nonnull BaseError error, @Nonnull Object... obj) {
        if (obj.length > 0) {
            for (Object o : obj) {
                if (BaseUtil.isNotEmpty(o)) {
                    throw new BaseException(error);
                }
            }
        }
    }

    /**
     * 判断是否是null或空,如果不是抛出
     *
     * @param msg 错误说明
     * @param obj 对象
     */
    public static void isNull(@Nonnull String msg, @Nonnull Object... obj) {
        if (obj.length > 0) {
            for (Object o : obj) {
                if (BaseUtil.isNotEmpty(o)) {
                    throw BaseException.fail(msg);
                }
            }
        }
    }

    /**
     * 判断是否不是空或null,是空抛出
     *
     * @param error 异常
     * @param obj   对象
     * @throws BaseException if the object is {@code null}
     */
    @Contract(value = "_,null->fail", pure = true)
    public static void notNull(@Nonnull BaseError error, @Nullable Object obj) {
        if (null == obj || BaseUtil.isEmpty(obj)) {
            throw new BaseException(error);
        }
    }

    /**
     * 判断是否不是空或null,是空抛出
     *
     * @param error 异常
     * @param obj1  对象1
     * @param obj2  对象2
     * @throws BaseException if the object is {@code null}
     */
    @Contract(value = "_,null,_->fail;_,_,null->fail", pure = true)
    public static void notNull(@Nonnull BaseError error, @Nullable Object obj1, @Nullable Object obj2) {
        if (null == obj1 || BaseUtil.isEmpty(obj1)) {
            throw new BaseException(error);
        }
        if (null == obj2 || BaseUtil.isEmpty(obj2)) {
            throw new BaseException(error);
        }
    }

    /**
     * 判断是否不是空或null,是空抛出
     *
     * @param error 异常
     * @param obj1  对象1
     * @param obj2  对象2
     * @param obj3  对象3
     * @throws BaseException if the object is {@code null}
     */
    @Contract(value = "_,null,_,_->fail;_,_,null,_->fail;_,_,_,null->fail", pure = true)
    public static void notNull(@Nonnull BaseError error, @Nullable Object obj1, @Nullable Object obj2, @Nullable Object obj3) {
        if (null == obj1 || BaseUtil.isEmpty(obj1)) {
            throw new BaseException(error);
        }
        if (null == obj2 || BaseUtil.isEmpty(obj2)) {
            throw new BaseException(error);
        }
        if (null == obj3 || BaseUtil.isEmpty(obj3)) {
            throw new BaseException(error);
        }
    }

    /**
     * 判断是否不是空或null,是空抛出
     *
     * @param error 异常
     * @param obj1  对象1
     * @param obj2  对象2
     * @param obj3  对象3
     * @param obj4  对象4
     * @throws BaseException if the object is {@code null}
     */
    @Contract(value = "_,null,_,_,_->fail;_,_,null,_,_->fail;_,_,_,null,_->fail;_,_,_,_,null->fail", pure = true)
    public static void notNull(@Nonnull BaseError error, @Nullable Object obj1, @Nullable Object obj2, @Nullable Object obj3, @Nullable Object obj4) {
        if (null == obj1 || BaseUtil.isEmpty(obj1)) {
            throw new BaseException(error);
        }
        if (null == obj2 || BaseUtil.isEmpty(obj2)) {
            throw new BaseException(error);
        }
        if (null == obj3 || BaseUtil.isEmpty(obj3)) {
            throw new BaseException(error);
        }
        if (null == obj4 || BaseUtil.isEmpty(obj4)) {
            throw new BaseException(error);
        }
    }

    /**
     * 判断是否不是空或null,是空抛出
     *
     * @param error 异常
     * @param obj1  对象1
     * @param obj2  对象2
     * @param obj3  对象3
     * @param obj4  对象4
     * @param obj5  对象5
     * @throws BaseException if the object is {@code null}
     */
    @Contract(value = "_,null,_,_,_,_->fail;_,_,null,_,_,_->fail;_,_,_,null,_,_->fail;_,_,_,_,null,_->fail;_,_,_,_,_,null->fail", pure = true)
    public static void notNull(@Nonnull BaseError error, @Nullable Object obj1, @Nullable Object obj2, @Nullable Object obj3, @Nullable Object obj4, @Nullable Object obj5) {
        if (null == obj1 || BaseUtil.isEmpty(obj1)) {
            throw new BaseException(error);
        }
        if (null == obj2 || BaseUtil.isEmpty(obj2)) {
            throw new BaseException(error);
        }
        if (null == obj3 || BaseUtil.isEmpty(obj3)) {
            throw new BaseException(error);
        }
        if (null == obj4 || BaseUtil.isEmpty(obj4)) {
            throw new BaseException(error);
        }
        if (null == obj5 || BaseUtil.isEmpty(obj5)) {
            throw new BaseException(error);
        }
    }

    /**
     * 判断是否不是空或null,是空抛出,注意,此方法无法配合进行findbugs静态分析,如果需要判断的值超过5个,请分开进行判断
     *
     * @param error 异常
     * @param obj   对象数组
     * @throws BaseException if the object is {@code null}
     */
    @Deprecated
    public static void notNull(@Nonnull BaseError error, @Nonnull Object... obj) {
        if (obj.length > 0) {
            for (Object o : obj) {
                if (BaseUtil.isEmpty(o)) {
                    throw new BaseException(error);
                }
            }
        }
    }

    /**
     * 判断是否不是空或null,是空抛出
     *
     * @param msg 错误说明
     * @param obj 对象
     * @throws BaseException if the object is {@code null}
     */
    @Contract("_, null -> fail")
    public static void notNull(@Nonnull String msg, @Nullable Object obj) {
        if (null == obj || BaseUtil.isEmpty(obj)) {
            throw BaseException.fail(msg);
        }
    }

    /**
     * 判断是否不是空或null,是空抛出
     *
     * @param msg  错误说明
     * @param obj1 对象1
     * @param obj2 对象2
     * @throws BaseException if the object is {@code null}
     */
    @Contract("_, null,_ -> fail;_, _,null -> fail")
    public static void notNull(@Nonnull String msg, @Nullable Object obj1, @Nullable Object obj2) {
        if (null == obj1 || BaseUtil.isEmpty(obj1)) {
            throw BaseException.fail(msg);
        }
        if (null == obj2 || BaseUtil.isEmpty(obj2)) {
            throw BaseException.fail(msg);
        }
    }

    /**
     * 判断是否不是空或null,是空抛出
     *
     * @param msg  错误说明
     * @param obj1 对象1
     * @param obj2 对象2
     * @param obj3 对象3
     * @throws BaseException if the object is {@code null}
     */
    @Contract("_, null,_,_ -> fail;_, _,null,_ -> fail;_, _,_,null -> fail")
    public static void notNull(@Nonnull String msg, @Nullable Object obj1, @Nullable Object obj2, @Nullable Object obj3) {
        if (null == obj1 || BaseUtil.isEmpty(obj1)) {
            throw BaseException.fail(msg);
        }
        if (null == obj2 || BaseUtil.isEmpty(obj2)) {
            throw BaseException.fail(msg);
        }
        if (null == obj3 || BaseUtil.isEmpty(obj3)) {
            throw BaseException.fail(msg);
        }
    }

    /**
     * 判断是否不是空或null,是空抛出
     *
     * @param msg  错误说明
     * @param obj1 对象1
     * @param obj2 对象2
     * @param obj3 对象3
     * @param obj4 对象4
     * @throws BaseException if the object is {@code null}
     */
    @Contract(value = "_,null,_,_,_->fail;_,_,null,_,_->fail;_,_,_,null,_->fail;_,_,_,_,null->fail", pure = true)
    public static void notNull(@Nonnull String msg, @Nullable Object obj1, @Nullable Object obj2, @Nullable Object obj3, @Nullable Object obj4) {
        if (null == obj1 || BaseUtil.isEmpty(obj1)) {
            throw BaseException.fail(msg);
        }
        if (null == obj2 || BaseUtil.isEmpty(obj2)) {
            throw BaseException.fail(msg);
        }
        if (null == obj3 || BaseUtil.isEmpty(obj3)) {
            throw BaseException.fail(msg);
        }
        if (null == obj4 || BaseUtil.isEmpty(obj4)) {
            throw BaseException.fail(msg);
        }
    }

    /**
     * 判断是否不是空或null,是空抛出
     *
     * @param msg  错误说明
     * @param obj1 对象1
     * @param obj2 对象2
     * @param obj3 对象3
     * @param obj4 对象4
     * @param obj5 对象5
     * @throws BaseException if the object is {@code null}
     */
    @Contract(value = "_,null,_,_,_,_->fail;_,_,null,_,_,_->fail;_,_,_,null,_,_->fail;_,_,_,_,null,_->fail;_,_,_,_,_,null->fail", pure = true)
    public static void notNull(@Nonnull String msg, @Nullable Object obj1, @Nullable Object obj2, @Nullable Object obj3, @Nullable Object obj4, @Nullable Object obj5) {
        if (null == obj1 || BaseUtil.isEmpty(obj1)) {
            throw BaseException.fail(msg);
        }
        if (null == obj2 || BaseUtil.isEmpty(obj2)) {
            throw BaseException.fail(msg);
        }
        if (null == obj3 || BaseUtil.isEmpty(obj3)) {
            throw BaseException.fail(msg);
        }
        if (null == obj4 || BaseUtil.isEmpty(obj4)) {
            throw BaseException.fail(msg);
        }
        if (null == obj5 || BaseUtil.isEmpty(obj5)) {
            throw BaseException.fail(msg);
        }
    }


    /**
     * 判断是否不是空或null,是空抛出,注意,此方法无法配合进行findbugs静态分析,如果需要判断的值超过5个,请分开进行判断
     *
     * @param msg 错误说明
     * @param obj 对象数组
     */
    @Deprecated
    @Contract(value = "_,null->fail", pure = true)
    public static void notNull(@Nonnull String msg, @Nonnull Object... obj) {
        if (obj.length > 0) {
            for (Object o : obj) {
                if (null == o || BaseUtil.isEmpty(o)) {
                    throw BaseException.fail(msg);
                }
            }
        }
    }

    /**
     * 判断不全是null或空,如果全是空,抛出
     *
     * @param error 异常
     * @param obj   对象
     */
    public static void notAllNull(@Nonnull BaseError error, @Nonnull Object... obj) {
        ok(error, isAllNull(obj));
    }

    /**
     * 判断不全是null或空,如果全是空,抛出
     *
     * @param msg 错误说明
     * @param obj 对象
     */
    public static void notAllNull(@Nonnull String msg, @Nonnull Object... obj) {
        ok(msg, isAllNull(obj));
    }

    /**
     * 如果不是空,则只需后续OK判断,如果OK判断false抛出
     *
     * @param error  异常
     * @param testOk 后续判断
     * @param obj    对象
     */
    public static void ifNotNull(@Nonnull BaseError error, @Nonnull Ok testOk, @Nonnull Object... obj) {
        if (isNotNull(obj)) {
            if (!testOk.ok()) {
                throw new BaseException(error);
            }
        }
    }

    /**
     * 如果不是空,则只需后续OK判断,如果OK判断false抛出
     *
     * @param msg    错误说明
     * @param testOk 后续判断
     * @param obj    对象
     */
    public static void ifNotNull(@Nonnull String msg, @Nonnull Ok testOk, @Nonnull Object... obj) {
        if (isNotNull(obj)) {
            if (!testOk.ok()) {
                throw BaseException.fail(msg);
            }
        }
    }

    /**
     * 先执行testOk判断,如果为true,则只需ifOk判断,如果false抛出
     *
     * @param error  异常
     * @param ifOK   异常判断
     * @param testOk 是否执行判断
     */
    @Contract(value = "_,true,false->fail", pure = true)
    public static void ifTest(@Nonnull BaseError error, @Nonnull Ok ifOK, @Nonnull Ok testOk) {
        if (ifOK.ok()) {
            ok(error, testOk.ok());
        }
    }

    /**
     * 先执行testOk判断,如果为true,则只需ifOk判断,如果false抛出
     *
     * @param msg    错误说明
     * @param ifOK   异常判断
     * @param testOk 是否执行判断
     */
    @Contract(value = "_,true,false->fail", pure = true)
    public static void ifTest(@Nonnull String msg, @Nonnull Ok ifOK, @Nonnull Ok testOk) {
        if (ifOK.ok()) {
            ok(msg, testOk.ok());
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

    private static boolean isNotNull(@Nonnull final Object... obj) {
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
