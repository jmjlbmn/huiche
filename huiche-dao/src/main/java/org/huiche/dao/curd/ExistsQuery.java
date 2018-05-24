package org.huiche.dao.curd;

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
public interface ExistsQuery<T> extends PathProvider<T>, SqlProvider {
    /**
     * 是否存在
     *
     * @param id ID
     * @return 是否存在
     */
    default boolean exists(@Nonnull Long id) {
        return sql().from(root()).where(pk().eq(id)).fetchCount() > 0;
    }

    /**
     * 是否存在
     *
     * @param predicate 条件
     * @return 是否存在
     */
    default boolean exists(@Nullable Predicate... predicate) {
        SQLQuery<?> query = sql().from(root());
        if (null != predicate && predicate.length > 0) {
            query = query.where(predicate);
        }
        return QueryUtil.count(query) > 0;
    }

    /**
     * 是否存在
     *
     * @param id        主键ID
     * @param predicate 条件
     * @return 是否存在
     */
    default boolean exists(@Nonnull Long id, @Nullable Predicate... predicate) {
        SQLQuery<?> query = sql().from(root()).where(pk().eq(id));
        if (null != predicate && predicate.length > 0) {
            query = query.where(predicate);
        }
        return QueryUtil.count(query) > 0;
    }
}
