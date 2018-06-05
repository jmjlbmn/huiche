package org.huiche.dao.curd;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.sql.SQLQuery;
import org.huiche.dao.provider.PathProvider;
import org.huiche.dao.provider.SqlProvider;
import org.huiche.dao.util.QueryUtil;

import javax.annotation.Nullable;

/**
 * 获取实体类操作,即 Select * ,建议使用GetColumns
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
    default T get(long id) {
        return get((OrderSpecifier[]) null, pk().eq(id));
    }

    /**
     * 获取单条数据
     *
     * @param predicate 条件
     * @return 数据
     */
    @Nullable
    default T get(@Nullable Predicate... predicate) {
        return get((OrderSpecifier[]) null, predicate);
    }

    /**
     * 获取单条数据
     *
     * @param id        主键ID
     * @param predicate 条件
     * @return 数据
     */
    @Nullable
    default T get(long id, @Nullable Predicate... predicate) {
        return get((OrderSpecifier[]) null, pk().eq(id), null == predicate ? null : ExpressionUtils.allOf(predicate));
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
        return get(null == order ? null : new OrderSpecifier[]{order}, predicate);
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
        SQLQuery<T> query = sql().selectFrom(root()).orderBy(null == order ? defaultMultiOrder() : order);
        if (null != predicate && predicate.length > 0) {
            query = query.where(predicate);
        }
        return QueryUtil.one(query);
    }
}
