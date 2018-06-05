package org.huiche.dao.curd;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.sql.SQLQuery;
import org.huiche.core.util.HuiCheUtil;
import org.huiche.dao.provider.PathProvider;
import org.huiche.dao.provider.SqlProvider;

import javax.annotation.Nullable;

/**
 * 是否存在操作
 *
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
        return exists(pk().eq(id));
    }

    /**
     * 是否存在
     *
     * @param id        主键ID
     * @param predicate 条件
     * @return 是否存在
     */
    default boolean exists(long id, @Nullable Predicate... predicate) {
        return exists(pk().eq(id), null == predicate ? null : ExpressionUtils.allOf(predicate));
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
        return HuiCheUtil.equals(1, query.fetchFirst());
    }
}
