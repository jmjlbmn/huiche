package org.huiche.dao.curd;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.sql.SQLQuery;
import org.huiche.dao.provider.PathProvider;
import org.huiche.dao.provider.SqlProvider;
import org.huiche.data.page.PageRequest;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * 获取实体类列表操作,即 Select * ,建议使用listColumns
 *
 * @author Maning
 */
public interface ListPageQuery<T> extends PathProvider<T>, SqlProvider {
    /**
     * 列表获取数据
     *
     * @param pageRequest 分页
     * @return 数据
     */
    @Nonnull
    default List<T> listPage(@Nullable PageRequest pageRequest) {
        return listPage(pageRequest, (OrderSpecifier<?>[]) null, (Predicate[]) null);
    }


    /**
     * 获取列表数据
     *
     * @param pageRequest 分页
     * @param order       排序
     * @return 数据列表
     */
    @Nonnull
    default List<T> listPage(@Nullable PageRequest pageRequest, @Nullable OrderSpecifier<?> order) {
        return listPage(pageRequest, order, (Predicate) null);
    }


    /**
     * 获取列表数据
     *
     * @param pageRequest 分页
     * @param predicate   条件
     * @return 数据列表
     */
    @Nonnull
    default List<T> listPage(@Nullable PageRequest pageRequest, @Nullable Predicate... predicate) {
        return listPage(pageRequest, (OrderSpecifier<?>[]) null, predicate);
    }

    /**
     * 获取列表数据
     *
     * @param order       排序
     * @param pageRequest 分页
     * @param predicate   条件
     * @return 数据列表
     */
    @Nonnull
    default List<T> listPage(@Nullable PageRequest pageRequest, @Nullable OrderSpecifier<?> order, @Nullable Predicate... predicate) {
        return listPage(pageRequest, null == order ? null : new OrderSpecifier[]{order}, predicate);
    }

    /**
     * 获取列表数据
     *
     * @param order       排序
     * @param pageRequest 分页
     * @param predicate   条件
     * @return 数据列表
     */
    @Nonnull
    default List<T> listPage(@Nullable PageRequest pageRequest, @Nullable OrderSpecifier<?>[] order, @Nullable Predicate... predicate) {
        SQLQuery<T> query = sql().selectFrom(root()).orderBy(null == order ? defaultMultiOrder() : order);
        if (null != predicate && predicate.length > 0) {
            query = query.where(predicate);
        }
        if (pageRequest == null) {
            pageRequest = new PageRequest();
        }
        query.offset(pageRequest.getOffset());
        query.limit(pageRequest.getRows());
        return query.fetch();
    }
}
