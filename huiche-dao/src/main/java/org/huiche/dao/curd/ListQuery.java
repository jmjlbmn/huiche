package org.huiche.dao.curd;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.sql.SQLQuery;
import org.huiche.core.util.StringUtil;
import org.huiche.dao.provider.PathProvider;
import org.huiche.dao.provider.SqlProvider;
import org.huiche.dao.util.QueryUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

/**
 * @author Maning
 */
public interface ListQuery<T> extends PathProvider<T>, SqlProvider {
    /**
     * 列表获取数据
     *
     * @return 数据
     */
    @Nonnull
    default List<T> list() {
        return QueryUtil.list(sql().selectFrom(root()).orderBy(defaultMultiOrder()));
    }

    /**
     * 根据ID列表获取数据
     *
     * @param ids 逗号分隔的ID列表
     * @return 数据
     */
    @Nonnull
    default List<T> list(@Nonnull String ids) {
        return QueryUtil.list(sql().selectFrom(root()).where(pk().in(StringUtil.split2ListLong(ids))).orderBy(defaultMultiOrder()));
    }

    /**
     * 根据ID列表获取数据
     *
     * @param ids ID列表
     * @return 数据
     */
    @Nonnull
    default List<T> list(@Nonnull Collection<Long> ids) {
        return QueryUtil.list(sql().selectFrom(root()).where(pk().in(ids)).orderBy(defaultMultiOrder()));
    }

    /**
     * 根据ID列表获取数据
     *
     * @param ids ID列表
     * @return 数据
     */
    @Nonnull
    default List<T> list(@Nonnull Long[] ids) {
        return QueryUtil.list(sql().selectFrom(root()).where(pk().in(ids)).orderBy(defaultMultiOrder()));
    }

    /**
     * 获取列表数据
     *
     * @param predicate 条件
     * @return 数据列表
     */
    @Nonnull
    default List<T> list(@Nullable Predicate... predicate) {
        SQLQuery<T> query = sql().selectFrom(root());
        if (null != predicate && predicate.length > 0) {
            query = query.where(predicate);
        }
        return QueryUtil.list(query.orderBy(defaultMultiOrder()));
    }

    /**
     * 获取列表数据
     *
     * @param order     排序
     * @param predicate 条件
     * @return 数据列表
     */
    @Nonnull
    default List<T> list(@Nullable OrderSpecifier<?> order, @Nullable Predicate... predicate) {
        return list(order, null, predicate);
    }

    /**
     * 获取列表数据
     *
     * @param order     排序
     * @param predicate 条件
     * @return 数据列表
     */
    @Nonnull
    default List<T> list(@Nullable OrderSpecifier[] order, @Nullable Predicate... predicate) {
        return list(order, null, predicate);
    }

    /**
     * 获取列表数据
     *
     * @param limit     获取条数
     * @param predicate 条件
     * @return 数据列表
     */
    @Nonnull
    default List<T> list(@Nullable Long limit, @Nullable Predicate... predicate) {
        SQLQuery<T> query = sql().selectFrom(root());
        if (null != predicate && predicate.length > 0) {
            query = query.where(predicate);
        }
        if (null != limit) {
            query = query.limit(limit);
        }
        return QueryUtil.list(query.orderBy(defaultMultiOrder()));
    }

    /**
     * 获取列表数据
     *
     * @param order     排序
     * @param limit     获取条数
     * @param predicate 条件
     * @return 数据列表
     */
    @Nonnull
    default List<T> list(@Nullable OrderSpecifier<?> order, @Nullable Long limit, @Nullable Predicate... predicate) {
        if (null == order) {
            return list(limit, predicate);
        } else {
            return list(new OrderSpecifier[]{order}, limit, predicate);
        }
    }

    /**
     * 获取列表数据
     *
     * @param order     排序
     * @param limit     获取条数
     * @param predicate 条件
     * @return 数据列表
     */
    @Nonnull
    default List<T> list(@Nullable OrderSpecifier[] order, @Nullable Long limit, @Nullable Predicate... predicate) {
        SQLQuery<T> query = sql().selectFrom(root());
        if (null != predicate && predicate.length > 0) {
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
