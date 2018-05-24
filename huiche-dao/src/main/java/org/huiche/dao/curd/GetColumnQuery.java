package org.huiche.dao.curd;

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
    default <Col> Col getColumn(@Nonnull Path<Col> column, long id) {
        return QueryUtil.one(sql().select(column).from(root()).where(pk().eq(id)));
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
        SQLQuery<Col> query = sql().select(column).from(root());
        if (null != predicate && predicate.length > 0) {
            query = query.where(predicate);
        }
        return QueryUtil.one(query.orderBy(defaultMultiOrder()));
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
    default <Col> Col getColumn(@Nonnull Path<Col> column, long id, @Nullable Predicate... predicate) {
        SQLQuery<Col> query = sql().select(column).from(root()).where(pk().eq(id));
        if (null != predicate && predicate.length > 0) {
            query = query.where(predicate);
        }
        return QueryUtil.one(query);
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
        if (null == order) {
            return getColumn(column, predicate);
        } else {
            return getColumn(column, new OrderSpecifier[]{order}, predicate);
        }
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
        SQLQuery<Col> query = sql().select(column).from(root());
        if (null != predicate && predicate.length > 0) {
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
