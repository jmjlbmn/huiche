package org.huiche.data.query;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.sql.RelationalPath;
import org.huiche.core.util.HuiCheUtil;
import org.huiche.data.entity.BaseEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

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
     * 条件处理,自定义值的处理方式
     *
     * @param check     值检查
     * @param predicate 条件
     * @param val       值
     * @param <T>       值类型
     * @return 条件
     */
    @Nullable
    default <T> Predicate predicate(@Nonnull java.util.function.Predicate<T> check, @Nonnull Supplier<Predicate> predicate, @Nullable T val) {
        if (check.test(val)) {
            return predicate.get();
        }
        return null;
    }

    /**
     * 排除某些列,注意,因性能考虑,应尽可能的定义常量来接收该方法的返回值,保证只需要执行一次
     *
     * @param columns 列
     * @param exclude 排除列
     * @return 排除后的列表
     */
    @Nonnull
    static Path<?>[] pathExclude(@Nonnull List<Path<?>> columns, @Nonnull Path<?>... exclude) {
        if (exclude.length > 0) {
            List<Path<?>> excludeList = Arrays.asList(exclude);
            columns.removeIf(excludeList::contains);
        }
        return columns.toArray(new Path[0]);
    }

    /**
     * 条件处理,自定义值的处理方式
     *
     * @param check     是否添加条件
     * @param predicate 值
     * @param <T>       值类型
     * @return 条件
     */
    @Nullable
    default <T> Predicate predicate(boolean check, @Nonnull Predicate predicate) {
        if (check) {
            return predicate;
        }
        return null;
    }

    /**
     * 条件处理,自定义值的处理方式
     *
     * @param check     是否添加条件
     * @param predicate 值
     * @param <T>       值类型
     * @return 条件
     */
    @Nullable
    default <T> Predicate predicate(@Nonnull Supplier<Boolean> check, @Nonnull Predicate predicate) {
        if (HuiCheUtil.equals(true, check.get())) {
            return predicate;
        }
        return null;
    }

    /**
     * 条件处理,自定义值的处理方式
     *
     * @param check     是否添加条件
     * @param predicate 值
     * @param <T>       值类型
     * @return 条件
     */
    @Nullable
    default <T> Predicate predicate(@Nonnull Supplier<Boolean> check, @Nonnull Supplier<Predicate> predicate) {
        if (HuiCheUtil.equals(true, check.get())) {
            return predicate.get();
        }
        return null;
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

    /**
     * 扩展增加dto继承
     *
     * @param beanPath 实体类的查询类
     * @param columns  扩展的列
     * @param <T>      实体类
     * @return 所有查询的列
     */
    @Nonnull
    static <T extends BaseEntity<T>> Expression[] extendColumn(@Nonnull RelationalPath<T> beanPath, @Nonnull Expression... columns) {
        List<Expression> list = new ArrayList<>();
        list.addAll(beanPath.getColumns());
        list.addAll(Arrays.asList(columns));
        return list.toArray(new Expression[0]);
    }

    /**
     * 获取继承bean的dto
     *
     * @param dtoClass dto的class
     * @param beanPath 实体类的查询类
     * @param columns  扩展的列
     * @param <T>      实体类
     * @param <DTO>    dto的类
     * @return dto
     */
    @Nonnull
    static <DTO extends T, T extends BaseEntity<T>> QBean<DTO> extendBean(@Nonnull Class<DTO> dtoClass, @Nonnull RelationalPath<T> beanPath, @Nonnull Expression... columns) {
        return Projections.fields(dtoClass, extendColumn(beanPath, columns));
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
        return predicate(HuiCheUtil::isNotEmpty, op, val);
    }
}
