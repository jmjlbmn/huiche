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
 * 获取实体类列表操作,即 Select * ,建议使用listColumns
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
        return list(null, (OrderSpecifier[]) null, (Predicate[]) null);
    }

    /**
     * 根据ID列表获取数据
     *
     * @param ids 逗号分隔的ID列表
     * @return 数据
     */
    @Nonnull
    default List<T> list(@Nonnull String ids) {
        return list(null, (OrderSpecifier[]) null, pk().in(StringUtil.split2ListLong(ids)));
    }

    /**
     * 根据ID列表获取数据
     *
     * @param ids ID列表
     * @return 数据
     */
    @Nonnull
    default List<T> list(@Nonnull Collection<Long> ids) {
        return list(null, (OrderSpecifier[]) null, pk().in(ids));
    }

    /**
     * 根据ID列表获取数据
     *
     * @param ids ID列表
     * @return 数据
     */
    @Nonnull
    default List<T> list(@Nonnull Long[] ids) {
        return list(null, (OrderSpecifier[]) null, pk().in(ids));
    }

    /**
     * 获取列表数据
     *
     * @param predicate 条件
     * @return 数据列表
     */
    @Nonnull
    default List<T> list(@Nullable Predicate... predicate) {
        return list(null, (OrderSpecifier[]) null, predicate);
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
        return list(null, null == order ? null : new OrderSpecifier[]{order}, predicate);
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
        return list(null, order, predicate);
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
        return list(limit, (OrderSpecifier[]) null, predicate);
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
    default List<T> list(@Nullable Long limit, @Nullable OrderSpecifier<?> order, @Nullable Predicate... predicate) {
        return list(limit, null == order ? null : new OrderSpecifier[]{order}, predicate);
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
    default List<T> list(@Nullable Long limit, @Nullable OrderSpecifier[] order, @Nullable Predicate... predicate) {
        SQLQuery<T> query = sql().selectFrom(root()).orderBy(null == order ? defaultMultiOrder() : order);
        if (null != predicate && predicate.length > 0) {
            query = query.where(predicate);
        }
        if (null != limit) {
            query = query.limit(limit);
        }
        return QueryUtil.list(query);
    }
}
