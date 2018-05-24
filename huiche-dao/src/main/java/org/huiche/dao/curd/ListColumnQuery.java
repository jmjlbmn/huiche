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
import java.util.List;

/**
 * @author Maning
 */
public interface ListColumnQuery<T> extends PathProvider<T>, SqlProvider {
    /**
     * 获取某个字段的列表
     *
     * @param column    字段
     * @param predicate 条件
     * @param <Col>     字段
     * @return 字段的列表
     */
    @Nonnull
    default  <Col> List<Col> listColumn(@Nonnull Path<Col> column, @Nullable Predicate... predicate) {
        return listColumn(column, (OrderSpecifier[]) null, null, predicate);
    }

    /**
     * 获取某个字段的列表
     *
     * @param column 字段
     * @param <Col>  字段
     * @return 字段的列表
     */
    @Nonnull
    default <Col> List<Col> listColumn(@Nonnull Path<Col> column) {
        return listColumn(column, (OrderSpecifier[]) null, null, (Predicate[]) null);
    }

    /**
     * 获取某个字段的列表
     *
     * @param column    字段
     * @param order     排序
     * @param predicate 条件
     * @param <Col>     字段
     * @return 字段的列表
     */
    @Nonnull
    default <Col> List<Col> listColumn(@Nonnull Path<Col> column, @Nullable OrderSpecifier<?> order, @Nullable Predicate... predicate) {
        return listColumn(column, order, null, predicate);
    }

    /**
     * 获取某个字段的列表
     *
     * @param column    字段
     * @param order     排序
     * @param predicate 条件
     * @param <Col>     字段
     * @return 字段的列表
     */
    @Nonnull
    default <Col> List<Col> listColumn(@Nonnull Path<Col> column, @Nullable OrderSpecifier[] order, @Nullable Predicate... predicate) {
        return listColumn(column, order, null, predicate);
    }

    /**
     * 获取某个字段的列表
     *
     * @param column    字段
     * @param order     排序
     * @param limit     获取数量
     * @param predicate 条件
     * @param <Col>     字段
     * @return 字段的列表
     */
    @Nonnull
    default <Col> List<Col> listColumn(@Nonnull Path<Col> column, @Nullable OrderSpecifier<?> order, @Nullable Long limit, @Nullable Predicate... predicate) {
        if (null == order) {
            return listColumn(column, (OrderSpecifier[]) null, limit, predicate);
        } else {
            return listColumn(column, new OrderSpecifier[]{order}, limit, predicate);
        }
    }

    /**
     * 获取某个字段的列表
     *
     * @param column    字段
     * @param order     排序
     * @param limit     获取数量
     * @param predicate 条件
     * @param <Col>     字段
     * @return 字段的列表
     */
    @Nonnull
    default <Col> List<Col> listColumn(@Nonnull Path<Col> column, @Nullable OrderSpecifier[] order, @Nullable Long limit, @Nullable Predicate... predicate) {
        SQLQuery<Col> query = sql().select(column).from(root());
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
