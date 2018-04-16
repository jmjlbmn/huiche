package org.huiche.core.search;


import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import org.huiche.core.util.BaseUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * 筛选接口
 *
 * @author Maning
 */
public interface Search {
    /**
     * 获取检索条件
     *
     * @return 获取检索条件
     */
    @Nullable
    Predicate get();

    /**
     * 条件构造器Builder
     */
    class Builder {
        private final List<Predicate> list;

        public Builder() {
            list = new ArrayList<>();
        }

        /**
         * 添加条件,自定义值的处理方式
         *
         * @param check 值检查
         * @param op    操作方法
         * @param val   值
         * @param <T>   值类型
         * @return 条件
         */
        @Nonnull
        public <T> Builder predicate(@Nonnull java.util.function.Predicate<T> check, @Nonnull Function<T, Predicate> op, @Nullable T val) {
            if (check.test(val)) {
                Predicate predicate = op.apply(val);
                list.add(predicate);
            }
            return this;
        }

        /**
         * 添加条件,一般情况下推荐使用此种方式,值非空时添加条件
         *
         * @param op  操作方法
         * @param val 值
         * @param <T> 值类型
         * @return 条件
         */
        @Nonnull
        public <T> Builder predicate(@Nonnull Function<T, Predicate> op, @Nullable T val) {
            return predicate(BaseUtil::isNotEmpty, op, val);
        }

        /**
         * 添加条件
         *
         * @param predicate 条件
         * @return 构造器
         */
        @Nonnull
        public Builder predicate(@Nonnull Predicate predicate) {
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
}
