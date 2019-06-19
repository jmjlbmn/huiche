package org.huiche.dao.curd;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.sql.SQLQuery;
import org.huiche.dao.provider.PathProvider;
import org.huiche.dao.provider.SqlProvider;
import org.huiche.dao.util.QueryUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 获取一个列操作
 *
 * @author Maning
 */
public interface GetColumnQuery<T> extends PathProvider<T>, SqlProvider {
    /**
     * 获取单个字段
     *
     * @param column 列
     * @param <Col>  列类型
     * @param id     id
     * @return 字段数据
     */
    @Nullable
    default <Col> Col getColumn(long id, @Nonnull Path<Col> column) {
        return getColumn(column, (OrderSpecifier[]) null, pk().eq(id));
    }

    /**
     * 获取单个字段
     *
     * @param column 列
     * @param <Col>  列类型
     * @param id     id
     * @return 字段数据
     */
    @Nullable
    default <Col> Col getColumnExt(long id, @Nonnull Expression<Col> column) {
        return getColumnExt(column, (OrderSpecifier[]) null, pk().eq(id));
    }

    /**
     * 获取单个字段
     *
     * @param column    列
     * @param <Col>     列类型
     * @param predicate 条件
     * @return 字段数据
     */
    @Nullable
    default <Col> Col getColumn(@Nonnull Path<Col> column, @Nullable Predicate... predicate) {
        return getColumn(column, (OrderSpecifier[]) null, predicate);
    }

    /**
     * 获取单个字段
     *
     * @param column    列
     * @param <Col>     列类型
     * @param predicate 条件
     * @return 字段数据
     */
    @Nullable
    default <Col> Col getColumnExt(@Nonnull Expression<Col> column, @Nullable Predicate... predicate) {
        return getColumnExt(column, (OrderSpecifier[]) null, predicate);
    }

    /**
     * 获取单个字段
     *
     * @param column    列
     * @param <Col>     列类型
     * @param id        主键ID
     * @param predicate 条件
     * @return 字段数据
     */
    @Nullable
    default <Col> Col getColumn(long id, @Nonnull Path<Col> column, @Nullable Predicate... predicate) {
        return getColumn(column, (OrderSpecifier[]) null, pk().eq(id), null == predicate ? null : ExpressionUtils.allOf(predicate));
    }

    /**
     * 获取单个字段
     *
     * @param column    列
     * @param <Col>     列类型
     * @param id        主键ID
     * @param predicate 条件
     * @return 字段数据
     */
    @Nullable
    default <Col> Col getColumnExt(long id, @Nonnull Expression<Col> column, @Nullable Predicate... predicate) {
        return getColumnExt(column, (OrderSpecifier[]) null, pk().eq(id), null == predicate ? null : ExpressionUtils.allOf(predicate));
    }

    /**
     * 获取单个字段
     *
     * @param column    列
     * @param <Col>     列类型
     * @param order     排序
     * @param predicate 条件
     * @return 字段数据
     */
    @Nullable
    default <Col> Col getColumn(@Nonnull Path<Col> column, @Nullable OrderSpecifier<?> order, @Nullable Predicate... predicate) {
        return getColumn(column, null == order ? null : new OrderSpecifier[]{order}, predicate);
    }

    /**
     * 获取单个字段
     *
     * @param column    列
     * @param <Col>     列类型
     * @param order     排序
     * @param predicate 条件
     * @return 字段数据
     */
    @Nullable
    default <Col> Col getColumnExt(@Nonnull Expression<Col> column, @Nullable OrderSpecifier<?> order, @Nullable Predicate... predicate) {
        return getColumnExt(column, null == order ? null : new OrderSpecifier[]{order}, predicate);
    }

    /**
     * 获取单个字段
     *
     * @param column    列
     * @param <Col>     列类型
     * @param order     排序
     * @param predicate 条件
     * @return 字段数据
     */
    @Nullable
    default <Col> Col getColumn(@Nonnull Path<Col> column, @Nullable OrderSpecifier[] order, @Nullable Predicate... predicate) {
        return getColumnExt(column, order, predicate);
    }

    /**
     * 获取单个字段
     *
     * @param column    列
     * @param <Col>     列类型
     * @param order     排序
     * @param predicate 条件
     * @return 字段数据
     */
    @Nullable
    default <Col> Col getColumnExt(@Nonnull Expression<Col> column, @Nullable OrderSpecifier[] order, @Nullable Predicate... predicate) {
        SQLQuery<Col> query = sql().select(column).from(root()).orderBy(null == order ? defaultMultiOrder() : order);
        if (null != predicate && predicate.length > 0) {
            query = query.where(predicate);
        }
        return QueryUtil.one(query);
    }
}
