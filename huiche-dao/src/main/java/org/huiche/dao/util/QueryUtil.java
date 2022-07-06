package org.huiche.dao.util;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparablePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.sql.SQLQuery;
import org.huiche.core.util.StringUtil;
import org.huiche.data.page.PageRequest;
import org.huiche.data.page.PageResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * QueryDsl工具类,提供list,page,one,count查询的封装,及PageRequest的排序解析
 *
 * @author Maning
 */
@SuppressWarnings("unchecked")
public class QueryUtil {
    /**
     * 分页查询
     *
     * @param pageRequest 分页请求
     * @param query       查询
     * @param <T>         类型
     * @return 数据
     */
    @Nonnull
    public static <T> PageResponse<T> page(@Nullable PageRequest pageRequest, @Nonnull SQLQuery<T> query) {
        if (null == pageRequest) {
            pageRequest = new PageRequest();
        }
        query.offset(pageRequest.getOffset());
        query.limit(pageRequest.getRows());
        QueryResults<T> results = query.fetchResults();
        return new PageResponse<T>().setRows(results.getResults()).setTotal(results.getTotal());
    }

    /**
     * 列表查询
     *
     * @param query 查询
     * @param <T>   类型
     * @return 数据
     */
    @Nonnull
    public static <T> List<T> list(@Nonnull SQLQuery<T> query) {
        return query.fetch();
    }


    /**
     * 获取一条数据
     *
     * @param query 查询
     * @param <T>   类型
     * @return 数据
     */
    @Nullable
    public static <T> T one(@Nonnull SQLQuery<T> query) {
        return query.fetchFirst();
    }

    /**
     * 解析分页请求的排序,仅支持单表(字段名不会重复)
     *
     * @param request 分页请求
     * @return 条件
     */
    @Nonnull
    public static OrderSpecifier[] parsePageRequest(@Nullable PageRequest request) {
        if (null != request) {
            String sort = request.getSort();
            String order = request.getOrder();
            if (null != sort && sort.contains(",")) {
                String[] sorts = sort.split(",");
                String[] orders = order.split(",");
                int length = sorts.length;
                if (length == orders.length) {
                    OrderSpecifier[] arr = new OrderSpecifier[length];
                    for (int i = 0; i < length; i++) {
                        arr[i] = parseOrder(sorts[i], orders[i]);
                    }
                    return arr;
                }
            } else if (StringUtil.isNotEmpty(sort)) {
                return new OrderSpecifier[]{parseOrder(sort, order)};
            }
        }
        return new OrderSpecifier[0];
    }


    /**
     * 解析排序
     *
     * @param sortStr  排序字段名
     * @param orderStr 正序倒序
     * @return 排序
     */
    @Nonnull
    private static OrderSpecifier<?> parseOrder(@Nonnull String sortStr, @Nonnull String orderStr) {
        String fieldName = StringUtil.toDb(sortStr);
        ComparablePath<? extends Comparable> path = Expressions.comparablePath(Comparable.class, fieldName);
        return Order.ASC.name().equalsIgnoreCase(orderStr) ? path.asc() : path.desc();
    }
}
