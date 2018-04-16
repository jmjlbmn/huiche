package org.huiche.core.search;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * 查询条件构造器
 *
 * @author Maning
 */
public class PredicateBuilder {
    private final List<Predicate> list;

    public PredicateBuilder() {
        list = new ArrayList<>();
    }

    /**
     * 添加条件,一般情况下推荐使用此种方式,会自动处理空值
     *
     * @param op  操作方法
     * @param val 值
     * @param <T> 值类型
     * @return 条件
     */
    @Nonnull
    public <T> PredicateBuilder predicate(Function<T, Predicate> op, @Nullable T val) {
        if (null != val) {
            Predicate predicate = op.apply(val);
            list.add(predicate);
        }
        return this;
    }

    /**
     * 添加条件
     *
     * @param predicate 条件
     * @return 构造器
     */
    @Nonnull
    public PredicateBuilder predicate(@Nonnull Predicate predicate) {
        list.add(predicate);
        return this;
    }

    /**
     * 构造筛选条件
     *
     * @return 筛选条件
     */
    @Nullable
    public Predicate build() {
        return ExpressionUtils.allOf(list);
    }
}
