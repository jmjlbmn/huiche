package org.huiche.core.util;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLSerializer;
import org.huiche.core.consts.Const;
import org.huiche.core.dao.QueryDsl;
import org.huiche.core.entity.BaseEntity;
import org.huiche.core.page.PageRequest;
import org.huiche.core.page.PageResponse;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Maning
 */
public class QueryDslUtil {

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

    public static <T> List<T> listFromPage(PageRequest pageRequest, SQLQuery<T> query) {
        if (null == pageRequest) {
            pageRequest = PageRequest.dft();
        }
        query.offset(pageRequest.getOffset());
        query.limit(pageRequest.getRows());
        return query.fetch();
    }

    public static <T> List<T> list(SQLQuery<T> query) {
        return query.fetch();
    }

    public static long count(SQLQuery<?> query) {
        return query.fetchCount();
    }

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
     * 解析筛选成条件,仅支持单表(或字段名不重复)
     *
     * @param search 检索
     * @param <T>    表
     * @return 条件
     */
    public static <T extends BaseEntity> Predicate parseSearch(T search) {
        if (null == search) {
            return null;
        }
        List<Predicate> predicates = new ArrayList<>();
        Class<? extends BaseEntity> tableClass = search.getClass();
        for (Method method : tableClass.getMethods()) {
            try {
                String name = method.getName();
                if (name.startsWith("get") && !"getClass".equals(name)) {
                    Object result = method.invoke(search);
                    if (null != result) {
                        Class<?> type = method.getReturnType();
                        String fieldName = StringUtil.getMethodName2FieldName(name);
                        if (Integer.class.isAssignableFrom(type)) {
                            Integer resultVal = (Integer) result;
                            predicates.add(Expressions.numberPath(Integer.class, fieldName).eq(resultVal));
                        } else if (Long.class.isAssignableFrom(type)) {
                            Long resultVal = (Long) result;
                            predicates.add(Expressions.numberPath(Long.class, fieldName).eq(resultVal));
                        } else if (String.class.isAssignableFrom(type)) {
                            String resultVal = (String) result;
                            predicates.add(Expressions.stringPath(fieldName).contains(resultVal));
                        } else if (Float.class.isAssignableFrom(type)) {
                            Float resultVal = (Float) result;
                            predicates.add(Expressions.numberPath(Float.class, fieldName).eq(resultVal));
                        } else if (Double.class.isAssignableFrom(type)) {
                            Double resultVal = (Double) result;
                            predicates.add(Expressions.numberPath(Double.class, fieldName).eq(resultVal));
                        }
                    }
                }
            } catch (Exception ignored) {
            }
        }
        if (predicates.isEmpty()) {
            return null;
        } else {
            return ExpressionUtils.allOf(predicates);
        }
    }

    /**
     * 解析分页请求的排序,仅支持单表(字段名不会重复)
     *
     * @param request 分页请求
     * @return 条件
     */
    public static OrderSpecifier[] parsePageRequest(@Nullable PageRequest request) {
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
     * 解析排序
     *
     * @param sortStr  排序字段名
     * @param orderStr 正序倒序
     * @return 排序
     */

    public static OrderSpecifier parseOrder(String sortStr, String orderStr) {
        Order order = Order.ASC.name().equalsIgnoreCase(orderStr) ? Order.ASC : Order.DESC;
        String fieldName = StringUtil.toDb(sortStr);
        return new OrderSpecifier<Comparable>(order, Expressions.comparablePath(Comparable.class, fieldName));
    }
}
