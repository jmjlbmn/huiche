package org.huiche.support;

import org.huiche.exception.HuicheIllegalArgumentException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 断言工具类
 *
 * @author Maning
 */
public class Assert {
    /**
     * 判断条件为true,否则抛异常
     *
     * @param msg        消息
     * @param expression 条件
     */
    @Contract(value = "_,false->fail", pure = true)
    public static void ok(@NotNull String msg, boolean expression) {
        if (!expression) {
            throw new HuicheIllegalArgumentException(msg);
        }
    }

    /**
     * 先判断先决条件,再判断表达式,false 抛异常
     *
     * @param msg        消息
     * @param preTest    先决条件
     * @param expression 后续条件
     */
    public static void ifTest(@NotNull String msg, boolean preTest, @NotNull Supplier<Boolean> expression) {
        if (preTest) {
            Boolean r = expression.get();
            if (r == null || !r) {
                throw new HuicheIllegalArgumentException(msg);
            }
        }
    }

    /**
     * 通过先决参数不为空,其后判断表达式,false 抛异常
     *
     * @param msg        消息
     * @param expression 表达式
     * @param args       先决判断的非空参数
     */
    public static void ifNotEmpty(@NotNull String msg, @NotNull Supplier<Boolean> expression, @NotNull Object... args) {
        for (Object arg : args) {
            if (checkEmpty(arg)) {
                return;
            }
        }
        Boolean r = expression.get();
        if (r == null || !r) {
            throw new HuicheIllegalArgumentException(msg);
        }

    }

    /**
     * 判断两个对象相等, 否则抛异常
     *
     * @param msg 消息
     * @param a   对象a
     * @param b   对象b
     * @param <T> 对象类型
     */
    public static <T> void equals(@NotNull String msg, @Nullable T a, @Nullable T b) {
        if (!checkEquals(a, b)) {
            throw new HuicheIllegalArgumentException(msg);
        }
    }

    /**
     * 判断两个对象不相等, 否则抛异常
     *
     * @param msg 消息
     * @param a   对象a
     * @param b   对象b
     */
    public static void notEquals(@NotNull String msg, @Nullable Object a, @Nullable Object b) {
        if (checkEquals(a, b)) {
            throw new HuicheIllegalArgumentException(msg);
        }
    }

    /**
     * 传入参数必须是空,否则抛异常
     *
     * @param msg 消息
     * @param obj 对象
     */
    @Contract(value = "_,!null->fail", pure = true)
    public static void isEmpty(@NotNull String msg, @Nullable Object obj) {
        if (!checkEmpty(obj)) {
            throw new HuicheIllegalArgumentException(msg);
        }
    }

    /**
     * 传入参数必须是空, 否则抛异常
     *
     * @param msg  消息
     * @param obj1 参数1
     * @param obj2 参数2
     */
    @Contract(value = "_,!null,_->fail;_,_,!null->fail;", pure = true)
    public static void isEmpty(@NotNull String msg, @Nullable Object obj1, @Nullable Object obj2) {
        isAllEmpty(msg, Arrays.asList(obj1, obj2));
    }

    /**
     * 传入参数必须是空, 否则抛异常
     *
     * @param msg  消息
     * @param obj1 参数1
     * @param obj2 参数2
     * @param obj3 参数3
     */
    @Contract(value = "_,!null,_,_->fail;_,_,!null,_->fail;_,_,_,!null->fail;", pure = true)
    public static void isEmpty(@NotNull String msg, @Nullable Object obj1, @Nullable Object obj2, @Nullable Object obj3) {
        isAllEmpty(msg, Arrays.asList(obj1, obj2, obj3));
    }

    /**
     * 判断是否为空对象,null或者集合数组的长度为0;
     *
     * @param obj 对象
     * @return 是否胃口
     */
    public static boolean checkEmpty(@Nullable Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof CharSequence) {
            return obj.toString().trim().isEmpty();
        }
        if (obj instanceof Iterable) {
            return ((Iterable<?>) obj).iterator().hasNext();
        }
        if (obj instanceof Iterator) {
            return ((Iterator<?>) obj).hasNext();
        }
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).isEmpty();
        }
        if (obj.getClass().isArray()) {
            return ((Object[]) obj).length == 0;
        }
        if (obj instanceof Enumeration) {
            return ((Enumeration<?>) obj).hasMoreElements();
        }
        return false;
    }

    /**
     * 判断是否相等
     *
     * @param a 参数a
     * @param b 参数b
     * @return 是否相等
     */
    public static boolean checkEquals(@Nullable Object a, @Nullable Object b) {
        boolean r = Objects.equals(a, b);
        if (!r && a instanceof Number && b instanceof Number) {
            r = ((Number) a).doubleValue() - ((Number) b).doubleValue() == 0;
        }
        return r;
    }

    /**
     * 判断参数不是空的, 否则抛异常
     *
     * @param msg 消息
     * @param obj 参数
     */
    @Contract(value = "_,null->fail", pure = true)
    public static void notEmpty(@NotNull String msg, @Nullable Object obj) {
        if (checkEmpty(obj)) {
            throw new HuicheIllegalArgumentException(msg);
        }
    }

    /**
     * 判断参数不是空的, 否则抛异常
     *
     * @param msg  消息
     * @param obj1 参数1
     * @param obj2 参数2
     */
    @Contract(value = "_,null,_->fail;_,_,null->fail", pure = true)
    public static void notEmpty(@NotNull String msg, @Nullable Object obj1, @Nullable Object obj2) {
        notAnyEmpty(msg, Arrays.asList(obj1, obj2));
    }

    /**
     * 判断参数不是空的, 否则抛异常
     *
     * @param msg  消息
     * @param obj1 参数1
     * @param obj2 参数2
     * @param obj3 参数3
     */
    @Contract(value = "_,null,_,_->fail;_,_,null,_->fail;_,_,_,null->fail", pure = true)
    public static void notEmpty(@NotNull String msg, @Nullable Object obj1, @Nullable Object obj2, @Nullable Object obj3) {
        notAnyEmpty(msg, Arrays.asList(obj1, obj2, obj3));
    }

    /**
     * 判断参数不是空的, 否则抛异常
     *
     * @param msg  消息
     * @param obj1 参数1
     * @param obj2 参数2
     * @param obj3 参数3
     * @param obj4 参数4
     */
    @Contract(value = "_,null,_,_,_->fail;_,_,null,_,_->fail;_,_,_,null,_->fail;_,_,_,_,null->fail", pure = true)
    public static void notEmpty(@NotNull String msg, @Nullable Object obj1, @Nullable Object obj2, @Nullable Object obj3, @Nullable Object obj4) {
        notAnyEmpty(msg, Arrays.asList(obj1, obj2, obj3, obj4));
    }

    /**
     * 判断参数不是空的, 否则抛异常,超过5个需要判断的值时,手动调用多次即可
     *
     * @param msg  消息
     * @param obj1 参数1
     * @param obj2 参数2
     * @param obj3 参数3
     * @param obj4 参数4
     * @param obj5 参数5
     */
    @Contract(value = "_,null,_,_,_,_->fail;_,_,null,_,_,_->fail;_,_,_,null,_,_->fail;_,_,_,_,null,_->fail;_,_,_,_,_,null->fail", pure = true)
    public static void notEmpty(@NotNull String msg, @Nullable Object obj1, @Nullable Object obj2, @Nullable Object obj3, @Nullable Object obj4, @Nullable Object obj5) {
        notAnyEmpty(msg, Arrays.asList(obj1, obj2, obj3, obj4, obj5));
    }

    /**
     * 判断先决条件不是空的, 其后判断其指定子属性, 不能是空的, 否则抛异常
     *
     * @param msg    消息
     * @param preObj 先决判断对象
     * @param checks 后续判断对象
     * @param <T>    泛型
     */
    public static <T> void notEmpty(@NotNull String msg, @Nullable T preObj, @NotNull Function<T, Collection<Object>> checks) {
        if (checkEmpty(preObj)) {
            throw new HuicheIllegalArgumentException(msg);
        }
        Collection<Object> list = checks.apply(preObj);
        if (checkEmpty(list)) {
            throw new HuicheIllegalArgumentException(msg);
        }
        notAnyEmpty(msg, list);
    }

    /**
     * 判断所有元素都是空的,否则抛异常
     *
     * @param msg  消息
     * @param args 参数
     */
    public static void isAllEmpty(@NotNull String msg, @NotNull Collection<Object> args) {
        for (Object arg : args) {
            if (!checkEmpty(arg)) {
                throw new HuicheIllegalArgumentException(msg);
            }
        }
    }


    /**
     * 判断所有元素全都是不是空的,否则抛异常
     *
     * @param msg  消息
     * @param args 参数
     */
    public static void notAllEmpty(@NotNull String msg, @NotNull Collection<Object> args) {
        boolean r = true;
        for (Object arg : args) {
            if (!checkEmpty(arg)) {
                r = false;
            }
        }
        if (r) {
            throw new HuicheIllegalArgumentException(msg);
        }
    }

    /**
     * 判断所有元素都不能是空的,否则抛异常
     *
     * @param msg  消息
     * @param args 参数列表
     */
    public static void notAnyEmpty(@NotNull String msg, @NotNull Collection<Object> args) {
        for (Object arg : args) {
            if (checkEmpty(arg)) {
                throw new HuicheIllegalArgumentException(msg);
            }
        }
    }

    /**
     * 判断指定对象是否在指定范围内
     *
     * @param msg    消息
     * @param src    源对象
     * @param target 目标对象
     * @param <T>    对象类型
     */
    public static <T> void isIn(@NotNull String msg, @Nullable T src, @NotNull List<T> target) {
        if (src != null) {
            for (T t : target) {
                if (checkEquals(src, t)) {
                    return;
                }
            }
        }
        throw new HuicheIllegalArgumentException(msg);
    }

    /**
     * 判断指定对象不在指定范围内
     *
     * @param msg    消息
     * @param src    源对象
     * @param target 目标对象
     * @param <T>    对象类型
     */
    public static <T> void notIn(@NotNull String msg, @Nullable T src, @NotNull List<T> target) {
        boolean r = src != null;
        if (r) {
            for (T t : target) {
                if (checkEquals(src, t)) {
                    r = false;
                }
            }
        }
        if (!r) {
            throw new HuicheIllegalArgumentException(msg);
        }
    }


}
