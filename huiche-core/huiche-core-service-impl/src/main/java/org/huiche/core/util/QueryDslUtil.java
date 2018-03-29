package org.huiche.core.util;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLSerializer;
import org.huiche.core.consts.Const;
import org.huiche.core.dao.QueryDsl;
import org.huiche.core.page.PageRequest;
import org.huiche.core.page.PageResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * QueryDsl工具类
 *
 * @author Maning
 */
public class QueryDslUtil {
    /**
     * 分页查询
     *
     * @param pageRequest 分页请求
     * @param query       查询
     * @param <T>         类型
     * @return 数据
     */
    public static <T> PageResponse<T> page(PageRequest pageRequest, SQLQuery<T> query) {
        if (null == pageRequest) {
            pageRequest = PageRequest.dft();
        }
        query.offset(pageRequest.getOffset());
        query.limit(pageRequest.getRows());
        SQLSerializer serializer = new SQLSerializer(QueryDsl.CONFIG);
        serializer.serialize(query.getMetadata(), true);
        QueryDsl.logSql(query.getMetadata(), serializer);
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
    public static <T> List<T> list(SQLQuery<T> query) {
        return query.fetch();
    }

    /**
     * 数量查询
     *
     * @param query 查询
     * @return 数量
     */
    public static long count(SQLQuery<?> query) {
        return query.fetchCount();
    }

    /**
     * 获取一条数据
     *
     * @param query 查询
     * @param <T>   类型
     * @return 数据
     */
    public static <T> T one(SQLQuery<T> query) {
        return query.fetchFirst();
    }

    /**
     * 排除某些列,注意,因性能考虑,应尽可能的定义常量来接收该方法的返回值,保证只需要执行一次
     *
     * @param columns 列
     * @param exclude 排除列
     * @return 排除后的列表
     */
    public static Path<?>[] pathExclude(List<Path<?>> columns, Path<?>... exclude) {
        if (null != exclude && exclude.length > 0) {
            List<Path<?>> excludeList = Arrays.asList(exclude);
            columns.removeIf(excludeList::contains);
        }
        return columns.toArray(new Path[0]);
    }

    /**
     * 解析分页请求的排序,仅支持单表(字段名不会重复)
     *
     * @param request 分页请求
     * @return 条件
     */
    public static OrderSpecifier[] parsePageRequest(PageRequest request) {
        if (null != request) {
            String sort = request.getSort();
            String order = request.getOrder();
            if (null != sort && sort.contains(Const.COMMA)) {
                String[] sorts = sort.split(Const.COMMA);
                String[] orders = order.split(Const.COMMA);
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
     * 解析分页请求的排序,仅支持单表(字段名不会重复)
     *
     * @param columnMap 可排序字段列表的map,key是属性名,val是对应的列,注意,因性能考虑,应尽可能的用常量或static块来定义此Map
     * @param request   分页请求
     * @return 条件
     */
    public static OrderSpecifier[] parsePageRequest(PageRequest request, Map<String, Expression<? extends Comparable>> columnMap) {
        if (null != request && null != columnMap && !columnMap.isEmpty()) {
            String sort = request.getSort();
            String order = request.getOrder();
            if (null != sort && sort.contains(Const.COMMA)) {
                String[] sorts = sort.split(Const.COMMA);
                String[] orders = order.split(Const.COMMA);
                int length = sorts.length;
                if (length == orders.length) {
                    List<OrderSpecifier<?>> list = new ArrayList<>();
                    for (int i = 0; i < length; i++) {
                        String column = sorts[i];
                        if (columnMap.containsKey(column)) {
                            list.add(new OrderSpecifier<>(Order.ASC.name().equalsIgnoreCase(orders[i]) ? Order.ASC : Order.DESC, columnMap.get(column)));
                        }
                    }
                    return list.toArray(new OrderSpecifier[0]);
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
    private static OrderSpecifier<?> parseOrder(String sortStr, String orderStr) {
        String fieldName = StringUtil.toDb(sortStr);
        return parseOrder(Expressions.comparablePath(Comparable.class, fieldName), orderStr);
    }

    /**
     * 解析排序
     *
     * @param sortColumn 排序字段
     * @param orderStr   正序倒序
     * @return 排序
     */
    private static OrderSpecifier<?> parseOrder(Expression<? extends Comparable> sortColumn, String orderStr) {
        Order order = Order.ASC.name().equalsIgnoreCase(orderStr) ? Order.ASC : Order.DESC;
        return new OrderSpecifier<>(order, sortColumn);
    }
}
