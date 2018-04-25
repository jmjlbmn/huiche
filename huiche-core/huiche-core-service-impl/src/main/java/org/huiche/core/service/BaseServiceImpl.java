package org.huiche.core.service;

import com.querydsl.core.types.Predicate;
import org.huiche.core.util.SearchUtil;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.validation.constraints.Null;
import java.util.function.Function;


/**
 * @author Maning
 */
@Transactional(rollbackFor = Exception.class, readOnly = true)
public class BaseServiceImpl implements BaseService {
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
    protected static <T> Predicate predicate(@Nonnull java.util.function.Predicate<T> check, @Nonnull Function<T, Predicate> op, @Nullable T val) {
        return SearchUtil.predicate(check, op, val);
    }

    /**
     * 条件处理,值非空条件条件
     *
     * @param op  操作方法
     * @param val 值
     * @param <T> 值类型
     * @return 条件
     */
    @Null
    protected static <T> Predicate predicate(@Nonnull Function<T, Predicate> op, @Nullable T val) {
        return SearchUtil.predicate(op, val);
    }

    /**
     * 用and组合多个条件
     *
     * @param predicate 多个条件
     * @return 最终条件
     */
    @Nullable
    protected static Predicate predicates(@Nonnull Predicate... predicate) {
        return SearchUtil.predicates(predicate);
    }

    /**
     * 等同于predicates
     *
     * @param predicate 多个条件
     * @return 最终条件
     * @see #predicates(Predicate...)
     */
    @Nullable
    protected static Predicate and(@Nonnull Predicate... predicate) {
        return SearchUtil.and(predicate);
    }

    /**
     * or
     *
     * @param left  条件1
     * @param right 条件2
     * @return 最终条件
     */
    @Nullable
    protected static Predicate or(@Nullable Predicate left, @Nullable Predicate right) {
        return SearchUtil.or(left, right);
    }
}
