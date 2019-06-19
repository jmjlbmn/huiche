package org.huiche.dao.curd;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
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

/**
 * 获取一个实体类中的几个列返回实体类操作
 *
 * @author Maning
 */
public interface GetColumnsQuery<T> extends PathProvider<T>, SqlProvider {
    /**
     * 查询单条数据的某些字段
     *
     * @param id      主键ID
     * @param columns 字段
     * @return 数据
     */
    @Nullable
    default T getColumns(long id, @Nonnull Path<?>... columns) {
        return getColumns(pk().eq(id), (OrderSpecifier[]) null, columns);
    }

    /**
     * 查询单条数据的某些字段
     *
     * @param id      主键ID
     * @param columns 字段
     * @return 数据
     */
    @Nullable
    default T getColumnsExt(long id, @Nonnull Expression<?>... columns) {
        return getColumnsExt(pk().eq(id), (OrderSpecifier[]) null, columns);
    }

    /**
     * 查询单条数据的某些字段
     *
     * @param predicate 条件
     * @param columns   字段
     * @return 数据
     */
    @Nullable
    default T getColumns(@Nullable Predicate predicate, @Nonnull Path<?>... columns) {
        return getColumns(predicate, (OrderSpecifier[]) null, columns);
    }

    /**
     * 查询单条数据的某些字段
     *
     * @param predicate 条件
     * @param columns   字段
     * @return 数据
     */
    @Nullable
    default T getColumnsExt(@Nullable Predicate predicate, @Nonnull Expression<?>... columns) {
        return getColumnsExt(predicate, (OrderSpecifier[]) null, columns);
    }

    /**
     * 查询单条数据的某些字段
     *
     * @param id        主键ID
     * @param predicate 条件
     * @param columns   字段
     * @return 数据
     */
    @Nullable
    default T getColumns(long id, @Nullable Predicate predicate, @Nonnull Path<?>... columns) {
        return getColumns(ExpressionUtils.allOf(pk().eq(id), predicate), (OrderSpecifier[]) null, columns);
    }

    /**
     * 查询单条数据的某些字段
     *
     * @param id        主键ID
     * @param predicate 条件
     * @param columns   字段
     * @return 数据
     */
    @Nullable
    default T getColumnsExt(long id, @Nullable Predicate predicate, @Nonnull Expression<?>... columns) {
        return getColumnsExt(ExpressionUtils.allOf(pk().eq(id), predicate), (OrderSpecifier[]) null, columns);
    }

    /**
     * 查询单条数据的某些字段
     *
     * @param predicate 条件
     * @param order     字段
     * @param columns   获取的字段
     * @return 数据
     */
    @Nullable
    default T getColumns(@Nullable Predicate predicate, @Nullable OrderSpecifier<?> order, @Nonnull Path<?>... columns) {
        return getColumns(predicate, null == order ? null : new OrderSpecifier[]{order}, columns);
    }

    /**
     * 查询单条数据的某些字段
     *
     * @param predicate 条件
     * @param order     字段
     * @param columns   获取的字段
     * @return 数据
     */
    @Nullable
    default T getColumnsExt(@Nullable Predicate predicate, @Nullable OrderSpecifier<?> order, @Nonnull Expression<?>... columns) {
        return getColumnsExt(predicate, null == order ? null : new OrderSpecifier[]{order}, columns);
    }

    /**
     * 查询单条数据的某些字段
     *
     * @param predicate 条件
     * @param order     字段
     * @param columns   获取的字段
     * @return 数据
     */
    @Nullable
    default T getColumns(@Nullable Predicate predicate, @Nullable OrderSpecifier[] order, @Nonnull Path<?>... columns) {
        return getColumnsExt(predicate, order, columns);
    }

    /**
     * 查询单条数据的某些字段
     *
     * @param predicate 条件
     * @param order     字段
     * @param columns   获取的字段
     * @return 数据
     */
    @Nullable
    default T getColumnsExt(@Nullable Predicate predicate, @Nullable OrderSpecifier[] order, @Nonnull Expression<?>... columns) {
        Assert.ok("要获取字段不能为空", columns.length > 0);
        SQLQuery<T> query = sql().select(
                Projections.fields(root(), columns)
        ).from(root()).orderBy(null == order ? defaultMultiOrder() : order);
        if (null != predicate) {
            query = query.where(predicate);
        }
        return QueryUtil.one(query);
    }
}
