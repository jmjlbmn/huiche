package org.huiche.dao.curd;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.sql.SQLQuery;
import org.huiche.core.util.HuiCheUtil;
import org.huiche.dao.provider.PathProvider;
import org.huiche.dao.provider.SqlProvider;

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
    default boolean exists(long id) {
        return HuiCheUtil.equals(Expressions.ONE, sql().selectOne().from(root()).where(pk().eq(id)).fetchFirst());
    }

    /**
     * 是否存在
     *
     * @param predicate 条件
     * @return 是否存在
     */
    default boolean exists(@Nullable Predicate... predicate) {
        SQLQuery<Integer> query = sql().selectOne().from(root());
        if (null != predicate && predicate.length > 0) {
            query = query.where(predicate);
        }
        return HuiCheUtil.equals(Expressions.ONE, query.fetchFirst());
    }

    /**
     * 是否存在
     *
     * @param id        主键ID
     * @param predicate 条件
     * @return 是否存在
     */
    default boolean exists(long id, @Nullable Predicate... predicate) {
        SQLQuery<Integer> query = sql().selectOne().from(root()).where(pk().eq(id));
        if (null != predicate && predicate.length > 0) {
            query = query.where(predicate);
        }
        return HuiCheUtil.equals(Expressions.ONE, query.fetchFirst());
    }
}
