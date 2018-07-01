package org.huiche.core.util;

import lombok.experimental.UtilityClass;
import org.huiche.core.enums.ValEnum;
import org.huiche.core.exception.BaseError;
import org.huiche.core.exception.HuiCheException;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;


/**
 * 断言工具类,常用于逻辑处理,抛出异常以便返回错误信息
 *
 * @author Maning
 */
@UtilityClass
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
            throw new HuiCheException(error);
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
            throw new HuiCheException(msg);
        }
    }

    /**
     * 判断是否相等,不相等抛出
     *
     * @param error 异常
     * @param <T>   类型
     * @param a     对象a
     * @param b     对象b
     */
    public static <T> void equals(@Nonnull BaseError error, @Nullable T a, @Nullable T b) {
        if (!HuiCheUtil.equals(a, b)) {
            throw new HuiCheException(error);
        }
    }

    /**
     * 判断是否相等,不相等抛出
     *
     * @param msg 错误说明
     * @param <T> 类型
     * @param a   对象a
     * @param b   对象b
     */
    public static <T> void equals(@Nonnull String msg, @Nullable T a, @Nullable T b) {
        if (!HuiCheUtil.equals(a, b)) {
            throw new HuiCheException(msg);
        }
    }

    /**
     * 判断是否不相等,相等抛出
     *
     * @param error 异常
     * @param <T>   类型
     * @param a     对象a
     * @param b     对象b
     */
    public static <T> void noEquals(@Nonnull BaseError error, @Nullable T a, @Nullable T b) {
        if (HuiCheUtil.equals(a, b)) {
            throw new HuiCheException(error);
        }
    }

    /**
     * 判断是否不相等,相等抛出
     *
     * @param msg 错误说明
     * @param <T> 类型
     * @param a   对象a
     * @param b   对象b
     */
    public static <T> void noEquals(@Nonnull String msg, @Nullable T a, @Nullable T b) {
        if (HuiCheUtil.equals(a, b)) {
            throw new HuiCheException(msg);
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
        if (HuiCheUtil.isNotEmpty(obj)) {
            throw new HuiCheException(error);
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
        if (HuiCheUtil.isNotEmpty(obj)) {
            throw new HuiCheException(msg);
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
                if (HuiCheUtil.isNotEmpty(o)) {
                    throw new HuiCheException(error);
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
                if (HuiCheUtil.isNotEmpty(o)) {
                    throw new HuiCheException(msg);
                }
            }
        }
    }

    /**
     * 判断是否不是空或null,是空抛出
     *
     * @param error 异常
     * @param obj   对象
     * @throws HuiCheException if the object is {@code null}
     */
    @Contract(value = "_,null->fail", pure = true)
    public static void notNull(@Nonnull BaseError error, @Nullable Object obj) {
        if (HuiCheUtil.isEmpty(obj)) {
            throw new HuiCheException(error);
        }
    }

    /**
     * 判断是否不是空或null,是空抛出
     *
     * @param error 异常
     * @param obj1  对象1
     * @param obj2  对象2
     * @throws HuiCheException if the object is {@code null}
     */
    @Contract(value = "_,null,_->fail;_,_,null->fail", pure = true)
    public static void notNull(@Nonnull BaseError error, @Nullable Object obj1, @Nullable Object obj2) {
        if (HuiCheUtil.isEmpty(obj1)) {
            throw new HuiCheException(error);
        }
        if (HuiCheUtil.isEmpty(obj2)) {
            throw new HuiCheException(error);
        }
    }

    /**
     * 判断是否不是空或null,是空抛出
     *
     * @param error 异常
     * @param obj1  对象1
     * @param obj2  对象2
     * @param obj3  对象3
     * @throws HuiCheException if the object is {@code null}
     */
    @Contract(value = "_,null,_,_->fail;_,_,null,_->fail;_,_,_,null->fail", pure = true)
    public static void notNull(@Nonnull BaseError error, @Nullable Object obj1, @Nullable Object obj2, @Nullable Object obj3) {
        if (HuiCheUtil.isEmpty(obj1)) {
            throw new HuiCheException(error);
        }
        if (HuiCheUtil.isEmpty(obj2)) {
            throw new HuiCheException(error);
        }
        if (HuiCheUtil.isEmpty(obj3)) {
            throw new HuiCheException(error);
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
     * @throws HuiCheException if the object is {@code null}
     */
    @Contract(value = "_,null,_,_,_->fail;_,_,null,_,_->fail;_,_,_,null,_->fail;_,_,_,_,null->fail", pure = true)
    public static void notNull(@Nonnull BaseError error, @Nullable Object obj1, @Nullable Object obj2, @Nullable Object obj3, @Nullable Object obj4) {
        if (HuiCheUtil.isEmpty(obj1)) {
            throw new HuiCheException(error);
        }
        if (HuiCheUtil.isEmpty(obj2)) {
            throw new HuiCheException(error);
        }
        if (HuiCheUtil.isEmpty(obj3)) {
            throw new HuiCheException(error);
        }
        if (HuiCheUtil.isEmpty(obj4)) {
            throw new HuiCheException(error);
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
     * @throws HuiCheException if the object is {@code null}
     */
    @Contract(value = "_,null,_,_,_,_->fail;_,_,null,_,_,_->fail;_,_,_,null,_,_->fail;_,_,_,_,null,_->fail;_,_,_,_,_,null->fail", pure = true)
    public static void notNull(@Nonnull BaseError error, @Nullable Object obj1, @Nullable Object obj2, @Nullable Object obj3, @Nullable Object obj4, @Nullable Object obj5) {
        if (HuiCheUtil.isEmpty(obj1)) {
            throw new HuiCheException(error);
        }
        if (HuiCheUtil.isEmpty(obj2)) {
            throw new HuiCheException(error);
        }
        if (HuiCheUtil.isEmpty(obj3)) {
            throw new HuiCheException(error);
        }
        if (HuiCheUtil.isEmpty(obj4)) {
            throw new HuiCheException(error);
        }
        if (HuiCheUtil.isEmpty(obj5)) {
            throw new HuiCheException(error);
        }
    }

    /**
     * 判断是否不是空或null,是空抛出
     *
     * @param msg 错误说明
     * @param obj 对象
     * @throws HuiCheException if the object is {@code null}
     */
    @Contract("_, null -> fail")
    public static void notNull(@Nonnull String msg, @Nullable Object obj) {
        if (HuiCheUtil.isEmpty(obj)) {
            throw new HuiCheException(msg);
        }
    }

    /**
     * 判断是否不是空或null,是空抛出
     *
     * @param msg  错误说明
     * @param obj1 对象1
     * @param obj2 对象2
     * @throws HuiCheException if the object is {@code null}
     */
    @Contract("_, null,_ -> fail;_, _,null -> fail")
    public static void notNull(@Nonnull String msg, @Nullable Object obj1, @Nullable Object obj2) {
        notNull(BaseError.fail(msg), obj1, obj2);
    }

    /**
     * 判断是否不是空或null,是空抛出
     *
     * @param msg  错误说明
     * @param obj1 对象1
     * @param obj2 对象2
     * @param obj3 对象3
     * @throws HuiCheException if the object is {@code null}
     */
    @Contract("_, null,_,_ -> fail;_, _,null,_ -> fail;_, _,_,null -> fail")
    public static void notNull(@Nonnull String msg, @Nullable Object obj1, @Nullable Object obj2, @Nullable Object obj3) {
        notNull(BaseError.fail(msg), obj1, obj2, obj3);
    }

    /**
     * 判断是否不是空或null,是空抛出
     *
     * @param msg  错误说明
     * @param obj1 对象1
     * @param obj2 对象2
     * @param obj3 对象3
     * @param obj4 对象4
     * @throws HuiCheException if the object is {@code null}
     */
    @Contract(value = "_,null,_,_,_->fail;_,_,null,_,_->fail;_,_,_,null,_->fail;_,_,_,_,null->fail", pure = true)
    public static void notNull(@Nonnull String msg, @Nullable Object obj1, @Nullable Object obj2, @Nullable Object obj3, @Nullable Object obj4) {
        notNull(BaseError.fail(msg), obj1, obj2, obj3, obj4);
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
     * @throws HuiCheException if the object is {@code null}
     */
    @Contract(value = "_,null,_,_,_,_->fail;_,_,null,_,_,_->fail;_,_,_,null,_,_->fail;_,_,_,_,null,_->fail;_,_,_,_,_,null->fail", pure = true)
    public static void notNull(@Nonnull String msg, @Nullable Object obj1, @Nullable Object obj2, @Nullable Object obj3, @Nullable Object obj4, @Nullable Object obj5) {
        notNull(BaseError.fail(msg), obj1, obj2, obj3, obj4, obj5);
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
     * @param error 异常
     * @param ok    正常的逻辑返回true,返回false则抛异常
     * @param obj   对象
     */
    public static void ifNotNull(@Nonnull BaseError error, @Nonnull Supplier<Boolean> ok, @Nonnull Object... obj) {
        if (isNotNull(obj)) {
            if (!HuiCheUtil.equals(true, ok.get())) {
                throw new HuiCheException(error);
            }
        }
    }

    /**
     * 如果不是空,则只需后续OK判断,如果OK判断false抛出
     *
     * @param msg 错误说明
     * @param ok  正常的逻辑返回true,返回false则抛异常
     * @param obj 对象
     */
    public static void ifNotNull(@Nonnull String msg, @Nonnull Supplier<Boolean> ok, @Nonnull Object... obj) {
        if (isNotNull(obj)) {
            if (!HuiCheUtil.equals(true, ok.get())) {
                throw new HuiCheException(msg);
            }
        }
    }

    /**
     * 先执行testOk判断,如果为true,则只需ifOk判断,如果false抛出
     *
     * @param error 错误说明
     * @param ok    正常的逻辑返回true,返回false则抛异常
     * @param test  是否执行判断
     */
    public static void ifTest(@Nonnull BaseError error, boolean test, @Nonnull Supplier<Boolean> ok) {
        if (test) {
            ok(error, HuiCheUtil.equals(true, ok.get()));
        }
    }

    /**
     * 先执行testOk判断,如果为true,则只需ifOk判断,如果false抛出
     *
     * @param msg  错误说明
     * @param ok   正常的逻辑返回true,返回false则抛异常
     * @param test 是否执行判断
     */
    public static void ifTest(@Nonnull String msg, boolean test, @Nonnull Supplier<Boolean> ok) {
        if (test) {
            ok(msg, HuiCheUtil.equals(true, ok.get()));
        }
    }

    /**
     * 是否在数组中
     *
     * @param error  错误
     * @param src    源对象
     * @param target 目标比较对象
     * @param <T>    类型
     */
    @SafeVarargs
    public static <T> void in(@Nonnull BaseError error, @Nullable T src, @Nonnull T... target) {
        ok(error, HuiCheUtil.in(src, target));
    }

    /**
     * 是否在数组中
     *
     * @param msg    错误
     * @param src    源对象
     * @param target 目标比较对象
     * @param <T>    类型
     */
    @SafeVarargs
    public static <T> void in(@Nonnull String msg, @Nullable T src, @Nonnull T... target) {
        ok(msg, HuiCheUtil.in(src, target));
    }

    /**
     * 是否在常量类中
     *
     * @param error 错误
     * @param src   源对象
     * @param clazz 常量类
     */
    public static void inConst(@Nonnull BaseError error, @Nullable Object src, @Nonnull Class<?> clazz) {
        ok(error, CheckUtil.inConstant(clazz, src));
    }

    /**
     * 是否在常量类中
     *
     * @param msg   错误
     * @param src   源对象
     * @param clazz 常量类
     */
    public static void inConst(@Nonnull String msg, @Nullable Object src, @Nonnull Class<?> clazz) {
        ok(msg, CheckUtil.inConstant(clazz, src));
    }

    /**
     * 是否在枚举中
     *
     * @param error 错误
     * @param <T>   类型
     * @param src   源对象
     * @param clazz 枚举类
     */
    public static <T extends Enum<T>> void inEnum(@Nonnull BaseError error, @Nullable String src, @Nonnull Class<T> clazz) {
        if (HuiCheUtil.isNotEmpty(src)) {
            try {
                Enum.valueOf(clazz, src);
            } catch (Exception e) {
                throw new HuiCheException(error);
            }
        } else {
            throw new HuiCheException(error);
        }
    }

    /**
     * 是否在枚举中
     *
     * @param msg   错误
     * @param <T>   类型
     * @param src   源对象
     * @param clazz 枚举类
     */
    public static <T extends Enum<T>> void inEnum(@Nonnull String msg, @Nullable String src, @Nonnull Class<T> clazz) {
        inEnum(BaseError.fail(msg), src, clazz);
    }

    /**
     * 是否在枚举中
     *
     * @param error 错误
     * @param <T>   枚举类型
     * @param src   源对象
     * @param clazz 枚举类
     */
    public static <T extends Enum<T> & ValEnum> void inValEnum(@Nonnull BaseError error, @Nullable Integer src, @Nonnull Class<T> clazz) {
        notNull(error, EnumUtil.of(src, clazz));
    }

    /**
     * 是否在枚举中
     *
     * @param msg   错误
     * @param <T>   类型
     * @param src   源对象
     * @param clazz 枚举类
     */
    public static <T extends Enum<T> & ValEnum> void inValEnum(@Nonnull String msg, @Nullable Integer src, @Nonnull Class<T> clazz) {
        notNull(msg, EnumUtil.of(src, clazz));
    }

    private static boolean isAllNull(@Nonnull Object... obj) {
        if (obj.length > 0) {
            for (Object o : obj) {
                if (HuiCheUtil.isNotEmpty(o)) {
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
                if (HuiCheUtil.isEmpty(o)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
