package org.huiche.core.util;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLSerializer;
import org.huiche.core.consts.Const;
import org.huiche.core.dao.QueryDsl;
import org.huiche.core.entity.BaseEntity;
import org.huiche.core.page.PageRequest;
import org.huiche.core.page.PageResponse;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Maning
 * @version 2017/8/9
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
     * 解析筛选成条件,仅支持单表
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

    public static <T> OrderSpecifier parsePageRequest(T bean, PageRequest request) {
        if (null == request) {
            return null;
        }
        String sort = request.getSort();
        String order = request.getOrder();
        if (sort.contains(Const.COMMA)) {

        } else {
        }
        return parseOrder(bean,sort, order);
    }

    public static <T> OrderSpecifier parseOrder(T bean, String sortStr, String orderStr) {
        if (null == sortStr) {
            return null;
        }
        String fieldName = StringUtil.toDb(sortStr);
        return new OrderSpecifier<Comparable>(Order.DESC,Expressions.comparablePath(Comparable.class,fieldName));
//        try {
//            Method method = bean.getClass().getMethod("get" + StringUtil.convertFristToUpperCase(sortStr));
//            Class<?> type = method.getReturnType();
//            Expression<?> sort = null;
//            if (Integer.class.isAssignableFrom(type)) {
//                sort = Expressions.numberPath(Integer.class, fieldName);
//            } else if (Long.class.isAssignableFrom(type)) {
//                sort = Expressions.numberPath(Long.class, fieldName);
//            } else if (Float.class.isAssignableFrom(type)) {
//                sort = Expressions.numberPath(Float.class, fieldName);
//            } else if (Double.class.isAssignableFrom(type)) {
//                sort = Expressions.numberPath(Double.class, fieldName);
//            } else if (String.class.isAssignableFrom(type)) {
//            }
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }
    }
}
