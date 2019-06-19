package org.huiche.dao.curd;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.sql.SQLQuery;
import org.huiche.core.util.Assert;
import org.huiche.dao.provider.PathProvider;
import org.huiche.dao.provider.SqlProvider;
import org.huiche.dao.util.QueryUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * 获取实体类的多个字段/列构成的实体类的列表操作
 *
 * @author Maning
 */
public interface ListColumnsQuery<T> extends PathProvider<T>, SqlProvider {
    /**
     * 获取某些字段的列表数据
     *
     * @param columns 获取的字段
     * @return 数据
     */
    @Nonnull
    default List<T> listColumns(@Nonnull Path<?>... columns) {
        return listColumns(null, null, (OrderSpecifier[]) null, columns);
    }
    /**
     * 获取某些字段的列表数据
     *
     * @param columns 获取的字段
     * @return 数据
     */
    @Nonnull
    default List<T> listColumnsExt(@Nonnull Expression<?>... columns) {
        return listColumnsExt(null, null, (OrderSpecifier[]) null, columns);
    }

    /**
     * 获取某些字段的列表数据
     *
     * @param order   排序
     * @param columns 获取的字段
     * @return 数据
     */
    @Nonnull
    default List<T> listColumns(@Nullable OrderSpecifier<?> order, @Nonnull Path<?>... columns) {
        return listColumns(null, null, null == order ? null : new OrderSpecifier[]{order}, columns);
    }
    /**
     * 获取某些字段的列表数据
     *
     * @param order   排序
     * @param columns 获取的字段
     * @return 数据
     */
    @Nonnull
    default List<T> listColumnsExt(@Nullable OrderSpecifier<?> order, @Nonnull Expression<?>... columns) {
        return listColumnsExt(null, null, null == order ? null : new OrderSpecifier[]{order}, columns);
    }

    /**
     * 获取某些字段的列表数据
     *
     * @param order   排序
     * @param columns 获取的字段
     * @return 数据
     */
    @Nonnull
    default List<T> listColumns(@Nullable OrderSpecifier[] order, @Nonnull Path<?>... columns) {
        return listColumns(null, null, order, columns);
    }
    /**
     * 获取某些字段的列表数据
     *
     * @param order   排序
     * @param columns 获取的字段
     * @return 数据
     */
    @Nonnull
    default List<T> listColumnsExt(@Nullable OrderSpecifier[] order, @Nonnull Expression<?>... columns) {
        return listColumnsExt(null, null, order, columns);
    }

    /**
     * 获取某些字段的列表数据
     *
     * @param predicate 条件
     * @param columns   获取的字段
     * @return 数据
     */
    @Nonnull
    default List<T> listColumns(@Nullable Predicate predicate, @Nonnull Path<?>... columns) {
        return listColumns(predicate, null, (OrderSpecifier[]) null, columns);
    }
    /**
     * 获取某些字段的列表数据
     *
     * @param predicate 条件
     * @param columns   获取的字段
     * @return 数据
     */
    @Nonnull
    default List<T> listColumnsExt(@Nullable Predicate predicate, @Nonnull Expression<?>... columns) {
        return listColumnsExt(predicate, null, (OrderSpecifier[]) null, columns);
    }

    /**
     * 获取某些字段的列表数据
     *
     * @param predicate 条件
     * @param order     排序
     * @param columns   获取的字段
     * @return 数据
     */
    @Nonnull
    default List<T> listColumns(@Nullable Predicate predicate, @Nullable OrderSpecifier<?> order, @Nonnull Path<?>... columns) {
        return listColumns(predicate, null, null == order ? null : new OrderSpecifier[]{order}, columns);
    }
    /**
     * 获取某些字段的列表数据
     *
     * @param predicate 条件
     * @param order     排序
     * @param columns   获取的字段
     * @return 数据
     */
    @Nonnull
    default List<T> listColumnsExt(@Nullable Predicate predicate, @Nullable OrderSpecifier<?> order, @Nonnull Expression<?>... columns) {
        return listColumnsExt(predicate, null, null == order ? null : new OrderSpecifier[]{order}, columns);
    }

    /**
     * 获取某些字段的列表数据
     *
     * @param predicate 条件
     * @param order     排序
     * @param columns   获取的字段
     * @return 数据
     */
    @Nonnull
    default List<T> listColumns(@Nullable Predicate predicate, @Nullable OrderSpecifier[] order, @Nonnull Path<?>... columns) {
        return listColumns(predicate, null, order, columns);
    }
    /**
     * 获取某些字段的列表数据
     *
     * @param predicate 条件
     * @param order     排序
     * @param columns   获取的字段
     * @return 数据
     */
    @Nonnull
    default List<T> listColumnsExt(@Nullable Predicate predicate, @Nullable OrderSpecifier[] order, @Nonnull Expression<?>... columns) {
        return listColumnsExt(predicate, null, order, columns);
    }

    /**
     * 获取某些字段的列表数据
     *
     * @param limit   条数
     * @param columns 获取的字段
     * @return 数据
     */
    @Nonnull
    default List<T> listColumns(@Nullable Long limit, @Nonnull Path<?>... columns) {
        return listColumns(null, limit, (OrderSpecifier[]) null, columns);
    }
    /**
     * 获取某些字段的列表数据
     *
     * @param limit   条数
     * @param columns 获取的字段
     * @return 数据
     */
    @Nonnull
    default List<T> listColumnsExt(@Nullable Long limit, @Nonnull Expression<?>... columns) {
        return listColumnsExt(null, limit, (OrderSpecifier[]) null, columns);
    }

    /**
     * 获取某些字段的列表数据
     *
     * @param limit   获取条数
     * @param order   排序
     * @param columns 获取的字段
     * @return 数据
     */
    @Nonnull
    default List<T> listColumns(@Nullable Long limit, @Nullable OrderSpecifier<?> order, @Nonnull Path<?>... columns) {
        return listColumns(null, limit, null == order ? null : new OrderSpecifier[]{order}, columns);
    }
    /**
     * 获取某些字段的列表数据
     *
     * @param limit   获取条数
     * @param order   排序
     * @param columns 获取的字段
     * @return 数据
     */
    @Nonnull
    default List<T> listColumnsExt(@Nullable Long limit, @Nullable OrderSpecifier<?> order, @Nonnull Expression<?>... columns) {
        return listColumnsExt(null, limit, null == order ? null : new OrderSpecifier[]{order}, columns);
    }

    /**
     * 获取某些字段的列表数据
     *
     * @param limit   获取条数
     * @param order   排序
     * @param columns 获取的字段
     * @return 数据
     */
    @Nonnull
    default List<T> listColumns(long limit, @Nullable OrderSpecifier[] order, @Nonnull Path<?>... columns) {
        return listColumns(null, limit, order, columns);
    }
    /**
     * 获取某些字段的列表数据
     *
     * @param limit   获取条数
     * @param order   排序
     * @param columns 获取的字段
     * @return 数据
     */
    @Nonnull
    default List<T> listColumnsExt(long limit, @Nullable OrderSpecifier[] order, @Nonnull Expression<?>... columns) {
        return listColumnsExt(null, limit, order, columns);
    }

    /**
     * 获取某些字段的列表数据
     *
     * @param predicate 条件
     * @param limit     条数
     * @param columns   获取的字段
     * @return 数据
     */
    @Nonnull
    default List<T> listColumns(@Nullable Predicate predicate, @Nullable Long limit, @Nonnull Path<?>... columns) {
        return listColumns(predicate, limit, (OrderSpecifier[]) null, columns);
    }
    /**
     * 获取某些字段的列表数据
     *
     * @param predicate 条件
     * @param limit     条数
     * @param columns   获取的字段
     * @return 数据
     */
    @Nonnull
    default List<T> listColumnsExt(@Nullable Predicate predicate, @Nullable Long limit, @Nonnull Expression<?>... columns) {
        return listColumnsExt(predicate, limit, (OrderSpecifier[]) null, columns);
    }

    /**
     * 获取某些字段的列表数据
     *
     * @param predicate 条件
     * @param order     排序
     * @param limit     条数
     * @param columns   获取的字段
     * @return 数据
     */
    @Nonnull
    default List<T> listColumns(@Nullable Predicate predicate, @Nullable Long limit, @Nullable OrderSpecifier<?> order, @Nonnull Path<?>... columns) {
        return listColumns(predicate, limit, null == order ? null : new OrderSpecifier[]{order}, columns);
    }
    /**
     * 获取某些字段的列表数据
     *
     * @param predicate 条件
     * @param order     排序
     * @param limit     条数
     * @param columns   获取的字段
     * @return 数据
     */
    @Nonnull
    default List<T> listColumnsExt(@Nullable Predicate predicate, @Nullable Long limit, @Nullable OrderSpecifier<?> order, @Nonnull Expression<?>... columns) {
        return listColumnsExt(predicate, limit, null == order ? null : new OrderSpecifier[]{order}, columns);
    }

    /**
     * 获取某些字段的列表数据
     *
     * @param predicate 条件
     * @param order     排序
     * @param limit     条数
     * @param columns   获取的字段
     * @return 数据
     */
    @Nonnull
    default List<T> listColumns(@Nullable Predicate predicate, @Nullable Long limit, @Nullable OrderSpecifier[] order, @Nonnull Path<?>... columns) {
        return listColumnsExt(predicate, limit, order, columns);
    }

    /**
     * 获取某些字段的列表数据
     *
     * @param predicate 条件
     * @param order     排序
     * @param limit     条数
     * @param columns   获取的字段
     * @return 数据
     */
    @Nonnull
    default List<T> listColumnsExt(@Nullable Predicate predicate, @Nullable Long limit, @Nullable OrderSpecifier[] order, @Nonnull Expression<?>... columns) {
        Assert.ok("要获取字段不能为空", columns.length > 0);
        SQLQuery<T> query = sql().select(
                Projections.fields(root(), columns)
        ).from(root()).orderBy(null == order ? defaultMultiOrder() : order);
        if (null != predicate) {
            query = query.where(predicate);
        }
        if (null != limit) {
            query = query.limit(limit);
        }
        return QueryUtil.list(query);
    }
}
