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

/**
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
        Assert.ok("要获取字段不能为空", columns.length > 0);
        return QueryUtil.one(sql()
                .select(Projections.fields(root(), columns))
                .from(root())
                .where(pk().eq(id)));
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
        Assert.ok("要获取字段不能为空", columns.length > 0);
        SQLQuery<T> query = sql().select(
                Projections.fields(root(), columns)
        ).from(root());
        if (null != predicate) {
            query = query.where(predicate);
        }
        return QueryUtil.one(query.orderBy(defaultMultiOrder()));
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
        Assert.ok("要获取字段不能为空", columns.length > 0);
        SQLQuery<T> query = sql()
                .select(Projections.fields(root(), columns))
                .from(root())
                .where(pk().eq(id));
        if (null != predicate) {
            query = query.where(predicate);
        }
        return QueryUtil.one(query);
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
        if (null == order) {
            return getColumns(predicate, columns);
        } else {
            return getColumns(predicate, new OrderSpecifier[]{order}, columns);
        }
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
        Assert.ok("要获取字段不能为空", columns.length > 0);
        SQLQuery<T> query = sql().select(
                Projections.fields(root(), columns)
        ).from(root());
        if (null != predicate) {
            query = query.where(predicate);
        }
        if (null == order) {
            query = query.orderBy(defaultMultiOrder());
        } else {
            query = query.orderBy(order);
        }
        return QueryUtil.one(query);
    }
}
