package org.huiche.core.query;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import org.huiche.core.util.BaseUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;

/**
 * @author Maning
 */
public interface Query {
    /**
     * 条件处理,自定义值的处理方式
     *
     * @param check 值检查
     * @param op    操作方法
     * @param val   值
     * @param <T>   值类型
     * @return 条件
     */
    @Nullable
    default <T> Predicate predicate(@Nonnull java.util.function.Predicate<T> check, @Nonnull Function<T, Predicate> op, @Nullable T val) {
        if (check.test(val)) {
            return op.apply(val);
        }
        return null;
    }

    /**
     * 条件处理,值非空条件条件
     *
     * @param op  操作方法
     * @param val 值
     * @param <T> 值类型
     * @return 条件
     */
    @Nullable
    default <T> Predicate predicate(@Nonnull Function<T, Predicate> op, @Nullable T val) {
        return predicate(BaseUtil::isNotEmpty, op, val);
    }

    /**
     * 用and组合多个条件
     *
     * @param predicate 多个条件
     * @return 最终条件
     */
    @Nullable
    default Predicate predicates(@Nonnull Predicate... predicate) {
        return ExpressionUtils.allOf(predicate);
    }

    /**
     * or
     *
     * @param left  条件1
     * @param right 条件2
     * @return 最终条件
     */
    @Nullable
    default Predicate or(@Nullable Predicate left, @Nullable Predicate right) {
        return ExpressionUtils.or(left, right);
    }
}
