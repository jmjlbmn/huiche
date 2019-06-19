package org.huiche.dao.curd;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.sql.SQLQuery;
import org.huiche.core.util.Assert;
import org.huiche.dao.provider.PathProvider;
import org.huiche.dao.provider.SqlProvider;
import org.huiche.dao.util.QueryUtil;
import org.huiche.data.page.PageRequest;
import org.huiche.data.page.PageResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 获取实体类的几个字段构成的实体类的分页数据操作
 *
 * @author Maning
 */
public interface PageColumnsQuery<T> extends PathProvider<T>, SqlProvider {
    /**
     * 分页获取某些字段的数据
     *
     * @param pageRequest 分页
     * @param columns     字段
     * @return 分页的数据
     */
    @Nonnull
    default PageResponse<T> pageColumns(@Nullable PageRequest pageRequest, @Nonnull Path<?>... columns) {
        return pageColumns(pageRequest, null, (OrderSpecifier[]) null, columns);
    }

    /**
     * 分页获取某些字段的数据
     *
     * @param pageRequest 分页
     * @param columns     字段
     * @return 分页的数据
     */
    @Nonnull
    default PageResponse<T> pageColumnsExt(@Nullable PageRequest pageRequest, @Nonnull Expression<?>... columns) {
        return pageColumnsExt(pageRequest, null, (OrderSpecifier[]) null, columns);
    }

    /**
     * 分页获取某些字段的数据
     *
     * @param predicate   条件
     * @param pageRequest 分页
     * @param columns     字段
     * @return 分页的数据
     */
    @Nonnull
    default PageResponse<T> pageColumns(@Nullable PageRequest pageRequest, @Nullable Predicate predicate, @Nonnull Path<?>... columns) {
        return pageColumns(pageRequest, predicate, (OrderSpecifier[]) null, columns);
    }

    /**
     * 分页获取某些字段的数据
     *
     * @param predicate   条件
     * @param pageRequest 分页
     * @param columns     字段
     * @return 分页的数据
     */
    @Nonnull
    default PageResponse<T> pageColumnsExt(@Nullable PageRequest pageRequest, @Nullable Predicate predicate, @Nonnull Expression<?>... columns) {
        return pageColumnsExt(pageRequest, predicate, (OrderSpecifier[]) null, columns);
    }

    /**
     * 分页获取某些字段的数据
     *
     * @param order       排序
     * @param pageRequest 分页
     * @param columns     字段
     * @return 分页的数据
     */
    @Nonnull
    default PageResponse<T> pageColumns(@Nullable PageRequest pageRequest, @Nullable OrderSpecifier<?> order, @Nonnull Path<?>... columns) {
        return pageColumns(pageRequest, null, null == order ? null : new OrderSpecifier[]{order}, columns);
    }

    /**
     * 分页获取某些字段的数据
     *
     * @param order       排序
     * @param pageRequest 分页
     * @param columns     字段
     * @return 分页的数据
     */
    @Nonnull
    default PageResponse<T> pageColumnsExt(@Nullable PageRequest pageRequest, @Nullable OrderSpecifier<?> order, @Nonnull Expression<?>... columns) {
        return pageColumnsExt(pageRequest, null, null == order ? null : new OrderSpecifier[]{order}, columns);
    }

    /**
     * 分页获取某些字段的数据
     *
     * @param order       排序
     * @param pageRequest 分页
     * @param columns     字段
     * @return 分页的数据
     */
    @Nonnull
    default PageResponse<T> pageColumns(@Nullable PageRequest pageRequest, @Nullable OrderSpecifier[] order, @Nonnull Path<?>... columns) {
        return pageColumns(pageRequest, null, order, columns);
    }

    /**
     * 分页获取某些字段的数据
     *
     * @param order       排序
     * @param pageRequest 分页
     * @param columns     字段
     * @return 分页的数据
     */
    @Nonnull
    default PageResponse<T> pageColumnsExt(@Nullable PageRequest pageRequest, @Nullable OrderSpecifier[] order, @Nonnull Expression<?>... columns) {
        return pageColumnsExt(pageRequest, null, order, columns);
    }

    /**
     * 分页获取某些字段的数据
     *
     * @param predicate   条件
     * @param order       排序
     * @param pageRequest 分页
     * @param columns     字段
     * @return 分页的数据
     */
    @Nonnull
    default PageResponse<T> pageColumns(@Nullable PageRequest pageRequest, @Nullable Predicate predicate, @Nullable OrderSpecifier<?> order, @Nonnull Path<?>... columns) {
        return pageColumns(pageRequest, predicate, null == order ? null : new OrderSpecifier[]{order}, columns);
    }

    /**
     * 分页获取某些字段的数据
     *
     * @param predicate   条件
     * @param order       排序
     * @param pageRequest 分页
     * @param columns     字段
     * @return 分页的数据
     */
    @Nonnull
    default PageResponse<T> pageColumnsExt(@Nullable PageRequest pageRequest, @Nullable Predicate predicate, @Nullable OrderSpecifier<?> order, @Nonnull Expression<?>... columns) {
        return pageColumnsExt(pageRequest, predicate, null == order ? null : new OrderSpecifier[]{order}, columns);
    }

    /**
     * 分页获取某些字段的数据
     *
     * @param predicate   条件
     * @param order       排序
     * @param pageRequest 分页
     * @param columns     字段
     * @return 分页的数据
     */
    @Nonnull
    default PageResponse<T> pageColumns(@Nullable PageRequest pageRequest, @Nullable Predicate predicate, @Nullable OrderSpecifier[] order, @Nonnull Path<?>... columns) {
        return pageColumnsExt(pageRequest, predicate, order, columns);
    }

    /**
     * 分页获取某些字段的数据
     *
     * @param predicate   条件
     * @param order       排序
     * @param pageRequest 分页
     * @param columns     字段
     * @return 分页的数据
     */
    @Nonnull
    default PageResponse<T> pageColumnsExt(@Nullable PageRequest pageRequest, @Nullable Predicate predicate, @Nullable OrderSpecifier[] order, @Nonnull Expression<?>... columns) {
        Assert.ok("查询字段不可为空", columns.length > 0);
        SQLQuery<T> query = sql().select(Projections.fields(root(), columns)).from(root());
        if (null != predicate) {
            query = query.where(predicate);
        }
        if (null != order) {
            query = query.orderBy(order);
        } else {
            OrderSpecifier[] orders = QueryUtil.parsePageRequest(pageRequest);
            if (orders.length > 0) {
                query = query.orderBy(orders);
            } else {
                query = query.orderBy(defaultMultiOrder());
            }
        }
        return QueryUtil.page(pageRequest, query);
    }
}
