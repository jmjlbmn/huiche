package org.huiche.dao.curd;

import com.querydsl.core.types.Expression;
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
 * 获取实体类单一字段/列的集合操作
 *
 * @author Maning
 */
public interface ListColumnQuery<T> extends PathProvider<T>, SqlProvider {
    /**
     * 获取某个字段的列表
     *
     * @param column 字段
     * @param <Col>  字段
     * @return 字段的列表
     */
    @Nonnull
    default <Col> List<Col> listColumn(@Nonnull Path<Col> column) {
        return listColumn(column, null, (OrderSpecifier[]) null, (Predicate[]) null);
    }

    /**
     * 获取某个字段的列表
     *
     * @param column 字段
     * @param <Col>  字段
     * @return 字段的列表
     */
    @Nonnull
    default <Col> List<Col> listColumnExt(@Nonnull Expression<Col> column) {
        return listColumnExt(column, null, (OrderSpecifier[]) null, (Predicate[]) null);
    }

    /**
     * 获取某个字段的列表
     *
     * @param column 字段
     * @param order  排序
     * @param <Col>  字段
     * @return 字段的列表
     */
    @Nonnull
    default <Col> List<Col> listColumn(@Nonnull Path<Col> column, OrderSpecifier<?> order) {
        return listColumn(column, null, null == order ? null : new OrderSpecifier[]{order}, (Predicate[]) null);
    }

    /**
     * 获取某个字段的列表
     *
     * @param column 字段
     * @param order  排序
     * @param <Col>  字段
     * @return 字段的列表
     */
    @Nonnull
    default <Col> List<Col> listColumnExt(@Nonnull Expression<Col> column, OrderSpecifier<?> order) {
        return listColumnExt(column, null, null == order ? null : new OrderSpecifier[]{order}, (Predicate[]) null);
    }

    /**
     * 获取某个字段的列表
     *
     * @param column 字段
     * @param order  排序
     * @param <Col>  字段
     * @return 字段的列表
     */
    @Nonnull
    default <Col> List<Col> listColumn(@Nonnull Path<Col> column, OrderSpecifier... order) {
        return listColumn(column, null, order, (Predicate[]) null);
    }

    /**
     * 获取某个字段的列表
     *
     * @param column 字段
     * @param order  排序
     * @param <Col>  字段
     * @return 字段的列表
     */
    @Nonnull
    default <Col> List<Col> listColumnExt(@Nonnull Expression<Col> column, OrderSpecifier... order) {
        return listColumnExt(column, null, order, (Predicate[]) null);
    }

    /**
     * 获取某个字段的列表
     *
     * @param column    字段
     * @param predicate 条件
     * @param <Col>     字段
     * @return 字段的列表
     */
    @Nonnull
    default <Col> List<Col> listColumn(@Nonnull Path<Col> column, @Nullable Predicate... predicate) {
        return listColumn(column, null, (OrderSpecifier[]) null, predicate);
    }

    /**
     * 获取某个字段的列表
     *
     * @param column    字段
     * @param predicate 条件
     * @param <Col>     字段
     * @return 字段的列表
     */
    @Nonnull
    default <Col> List<Col> listColumnExt(@Nonnull Expression<Col> column, @Nullable Predicate... predicate) {
        return listColumnExt(column, null, (OrderSpecifier[]) null, predicate);
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
        return listColumn(column, null, null == order ? null : new OrderSpecifier[]{order}, predicate);
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
    default <Col> List<Col> listColumnExt(@Nonnull Expression<Col> column, @Nullable OrderSpecifier<?> order, @Nullable Predicate... predicate) {
        return listColumnExt(column, null, null == order ? null : new OrderSpecifier[]{order}, predicate);
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
        return listColumn(column, null, order, predicate);
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
    default <Col> List<Col> listColumnExt(@Nonnull Expression<Col> column, @Nullable OrderSpecifier[] order, @Nullable Predicate... predicate) {
        return listColumnExt(column, null, order, predicate);
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
    default <Col> List<Col> listColumn(@Nonnull Path<Col> column, @Nullable Long limit, @Nullable OrderSpecifier<?> order, @Nullable Predicate... predicate) {
        return listColumn(column, limit, null == order ? null : new OrderSpecifier[]{order}, predicate);
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
    default <Col> List<Col> listColumnExt(@Nonnull Expression<Col> column, @Nullable Long limit, @Nullable OrderSpecifier<?> order, @Nullable Predicate... predicate) {
        return listColumnExt(column, limit, null == order ? null : new OrderSpecifier[]{order}, predicate);
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
    default <Col> List<Col> listColumn(@Nonnull Path<Col> column, @Nullable Long limit, @Nullable OrderSpecifier[] order, @Nullable Predicate... predicate) {
        return listColumnExt(column, limit, order, predicate);
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
    default <Col> List<Col> listColumnExt(@Nonnull Expression<Col> column, @Nullable Long limit, @Nullable OrderSpecifier[] order, @Nullable Predicate... predicate) {
        SQLQuery<Col> query = sql().select(column).from(root()).orderBy(null == order ? defaultMultiOrder() : order);
        if (null != predicate && predicate.length > 0) {
            query = query.where(predicate);
        }
        if (null != limit) {
            query = query.limit(limit);
        }
        return QueryUtil.list(query);
    }
}
