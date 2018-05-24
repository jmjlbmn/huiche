package org.huiche.dao.curd;

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
        Assert.ok("查询字段不可为空", columns.length > 0);
        return QueryUtil.page(pageRequest, sql().select(Projections.fields(root(), columns)).from(root()).orderBy(defaultMultiOrder()));
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
    default PageResponse<T> pageColumns(@Nullable Predicate predicate, @Nullable PageRequest pageRequest, @Nonnull Path<?>... columns) {
        return pageColumns(predicate, (OrderSpecifier[]) null, pageRequest, columns);
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
    default PageResponse<T> pageColumns(@Nullable OrderSpecifier<?> order, @Nullable PageRequest pageRequest, @Nonnull Path<?>... columns) {
        return pageColumns(null, order, pageRequest, columns);
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
    default PageResponse<T> pageColumns(@Nullable OrderSpecifier[] order, @Nullable PageRequest pageRequest, @Nonnull Path<?>... columns) {
        return pageColumns(null, order, pageRequest, columns);
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
    default PageResponse<T> pageColumns(@Nullable Predicate predicate, @Nullable OrderSpecifier<?> order, @Nullable PageRequest pageRequest, @Nonnull Path<?>... columns) {
        if (null == order) {
            return pageColumns(predicate, (OrderSpecifier[]) null, pageRequest, columns);
        } else {
            return pageColumns(predicate, new OrderSpecifier[]{order}, pageRequest, columns);
        }
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
    default PageResponse<T> pageColumns(@Nullable Predicate predicate, @Nullable OrderSpecifier[] order, @Nullable PageRequest pageRequest, @Nonnull Path<?>... columns) {
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
