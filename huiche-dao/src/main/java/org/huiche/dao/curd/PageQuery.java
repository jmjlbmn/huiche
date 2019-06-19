package org.huiche.dao.curd;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.sql.SQLQuery;
import org.huiche.dao.provider.PathProvider;
import org.huiche.dao.provider.SqlProvider;
import org.huiche.dao.util.QueryUtil;
import org.huiche.data.page.PageRequest;
import org.huiche.data.page.PageResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 分页获取实体类操作,即 Select * ,建议使用pageColumns
 *
 * @author Maning
 */
public interface PageQuery<T> extends PathProvider<T>, SqlProvider {
    /**
     * 获取分页数据,默认方法,表结构简单时可以调用,结构复杂时请务必选择pageColumns
     *
     * @param pageRequest 分页请求
     * @return 分页数据
     */
    @Nonnull
    default PageResponse<T> page(@Nullable PageRequest pageRequest) {
        return page(pageRequest, (Predicate[]) null);
    }

    /**
     * 获取分页数据,默认方法,表结构简单时可以调用,结构复杂时请务必选择pageColumns
     *
     * @param pageRequest 分页请求
     * @param predicate   条件
     * @return 分页数据
     */
    @Nonnull
    default PageResponse<T> page(@Nullable PageRequest pageRequest, @Nullable Predicate... predicate) {
        return page(pageRequest, (OrderSpecifier[]) null, predicate);
    }

    /**
     * 获取分页数据,默认方法,表结构简单时可以调用,结构复杂时请务必选择pageColumns
     *
     * @param pageRequest 分页请求
     * @param predicate   条件
     * @param order       排序
     * @return 分页数据
     */
    @Nonnull
    default PageResponse<T> page(@Nullable PageRequest pageRequest, @Nullable OrderSpecifier order, @Nullable Predicate... predicate) {
        return page(pageRequest, null == order ? null : new OrderSpecifier[]{order}, predicate);
    }

    /**
     * 获取分页数据,默认方法,表结构简单时可以调用,结构复杂时请务必选择pageColumns
     *
     * @param pageRequest 分页请求
     * @param predicate   条件
     * @param order       排序
     * @return 分页数据
     */
    @Nonnull
    default PageResponse<T> page(@Nullable PageRequest pageRequest, @Nullable OrderSpecifier[] order, @Nullable Predicate... predicate) {
        SQLQuery<T> query = sql().selectFrom(root());
        if (null != predicate && predicate.length > 0) {
            query = query.where(predicate);
        }
        if (order == null || order.length == 0) {
            order = QueryUtil.parsePageRequest(pageRequest);
        }
        if (order.length > 0) {
            query = query.orderBy(order);
        } else {
            query = query.orderBy(defaultMultiOrder());
        }
        return QueryUtil.page(pageRequest, query);
    }
}
