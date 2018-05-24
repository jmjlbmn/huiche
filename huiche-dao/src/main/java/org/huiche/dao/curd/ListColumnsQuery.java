package org.huiche.dao.curd;

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
        Assert.ok("要获取字段不能为空", columns.length > 0);
        SQLQuery<T> query = sql().select(
                Projections.fields(root(), columns)
        ).from(root());
        return QueryUtil.list(sql().select(
                Projections.fields(root(), columns)
        ).from(root()).orderBy(defaultMultiOrder()));
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
        return listColumns(predicate, (OrderSpecifier<?>) null, columns);
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
        return listColumns(null, order, columns);
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
        return listColumns(predicate, order, null, columns);
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
        return listColumns(predicate, (OrderSpecifier[]) null, limit, columns);
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
    default List<T> listColumns(@Nullable Predicate predicate, @Nullable OrderSpecifier<?> order, @Nullable Long limit, @Nonnull Path<?>... columns) {
        if (null == order) {
            return listColumns(predicate, (OrderSpecifier[]) null, limit, columns);
        } else {
            return listColumns(predicate, new OrderSpecifier[]{order}, limit, columns);
        }
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
    default List<T> listColumns(@Nullable Predicate predicate, @Nullable OrderSpecifier[] order, @Nullable Long limit, @Nonnull Path<?>... columns) {
        Assert.ok("要获取字段不能为空", columns.length > 0);
        SQLQuery<T> query = sql().select(
                Projections.fields(root(), columns)
        ).from(root());
        if (null != predicate) {
            query = query.where(predicate);
        }
        if (null != order) {
            query = query.orderBy(order);
        } else {
            query = query.orderBy(defaultMultiOrder());
        }
        if (null != limit) {
            query = query.limit(limit);
        }
        return QueryUtil.list(query);
    }
}
