package org.huiche.dao.curd;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.sql.SQLQuery;
import org.huiche.dao.provider.PathProvider;
import org.huiche.dao.provider.SqlProvider;
import org.huiche.dao.util.QueryUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Maning
 */
public interface GetQuery<T> extends PathProvider<T>, SqlProvider {
    /**
     * 通过主键查找
     *
     * @param id 主键
     * @return 实体
     */
    @Nullable
    default T get(@Nonnull Long id) {
        return QueryUtil.one(sql().selectFrom(root()).where(pk().eq(id)));
    }

    /**
     * 获取单条数据
     *
     * @param predicate 条件
     * @return 数据
     */
    @Nullable
    default T get(@Nullable Predicate... predicate) {
        SQLQuery<T> query = sql().selectFrom(root());
        if (null != predicate && predicate.length > 0) {
            query = query.where(predicate);
        }
        return QueryUtil.one(query);
    }

    /**
     * 获取单条数据
     *
     * @param id        主键ID
     * @param predicate 条件
     * @return 数据
     */
    @Nullable
    default T get(@Nonnull Long id, @Nullable Predicate... predicate) {
        SQLQuery<T> query = sql().selectFrom(root()).where(pk().eq(id));
        if (null != predicate && predicate.length > 0) {
            query = query.where(predicate);
        }
        return QueryUtil.one(query);
    }

    /**
     * 获取单条数据,有排序
     *
     * @param order     排序
     * @param predicate 条件
     * @return 数据
     */
    @Nullable
    default T get(@Nullable OrderSpecifier<?> order, @Nullable Predicate... predicate) {
        if (null == order) {
            return get(predicate);
        } else {
            return get(new OrderSpecifier[]{order}, predicate);
        }
    }

    /**
     * 获取单条数据,有排序
     *
     * @param order     排序
     * @param predicate 条件
     * @return 数据
     */
    @Nullable
    default T get(@Nullable OrderSpecifier[] order, @Nullable Predicate... predicate) {
        SQLQuery<T> query = sql().selectFrom(root());
        if (null != predicate && predicate.length > 0) {
            query = query.where(predicate);
        }
        if (null != order) {
            query = query.orderBy(order);
        }
        return QueryUtil.one(query);
    }
}
