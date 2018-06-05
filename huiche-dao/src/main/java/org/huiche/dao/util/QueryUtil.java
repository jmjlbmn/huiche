package org.huiche.dao.util;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Operator;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.ComparablePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLSerializer;
import org.huiche.core.consts.Const;
import org.huiche.core.util.StringUtil;
import org.huiche.dao.QueryDsl;
import org.huiche.data.entity.BaseEntity;
import org.huiche.data.page.PageRequest;
import org.huiche.data.page.PageResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    @Nonnull
    public static <T> List<T> list(@Nonnull SQLQuery<T> query) {
        return query.fetch();
    }

    /**
     * 数量查询
     *
     * @param query 查询
     * @return 数量
     */
    public static long count(@Nonnull SQLQuery<?> query) {
        SQLSerializer serializer = new SQLSerializer(QueryDsl.CONFIG);
        serializer.serialize(query.getMetadata(), true);
        QueryDsl.logSql(query.getMetadata(), serializer);
        return query.fetchCount();
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
    @Nonnull
    public static OrderSpecifier[] parsePageRequest(@Nullable PageRequest request, @Nonnull Map<String, Expression<? extends Comparable>> columnMap) {
        if (null != request && !columnMap.isEmpty()) {
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
    @Nonnull
    private static OrderSpecifier<?> parseOrder(@Nonnull String sortStr, @Nonnull String orderStr) {
        String fieldName = StringUtil.toDb(sortStr);
        ComparablePath<? extends Comparable> path = Expressions.comparablePath(Comparable.class, fieldName);
        return Order.ASC.name().equalsIgnoreCase(orderStr) ? path.asc() : path.desc();
    }

    private static final String STAR = "*";
    private static final String LINE = "_";
    private static final String PERCENT = "%";

    /**
     * 解析筛选成条件,仅支持单表(或字段名不重复)
     *
     * @param entity 检索
     * @param <T>    表
     * @return 条件
     */
    @Nullable
    public static <T extends BaseEntity> Predicate ofEntity(@Nullable T entity) {
        if (null == entity) {
            return null;
        }
        List<Predicate> predicates = new ArrayList<>();
        Class<? extends BaseEntity> tableClass = entity.getClass();
        String tableName = StringUtil.toDb(tableClass.getSimpleName());
        for (Method method : tableClass.getMethods()) {
            try {
                String name = method.getName();
                if (name.startsWith("get") && !"getClass".equals(name) && !"get".equals(name) && method.getParameterCount() == 0) {
                    Object value = method.invoke(entity);
                    if (null != value) {
                        String fieldName = StringUtil.getMethodName2FieldName(name);
                        if (value instanceof String) {
                            String valStr = (String) value;
                            if (StringUtil.isNotEmpty(valStr)) {
                                if (valStr.contains(STAR) || valStr.contains(LINE)) {
                                    predicates.add(predicate(tableName, fieldName, Ops.LIKE_IC, ConstantImpl.create(valStr.replaceAll("\\*", PERCENT))));
                                } else {
                                    predicates.add(predicate(tableName, fieldName, Ops.STRING_CONTAINS_IC, ConstantImpl.create(value)));
                                }
                            }
                        } else if (value instanceof Number || value instanceof Boolean) {
                            predicates.add(predicate(tableName, fieldName, Ops.EQ, ConstantImpl.create(value)));
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

    @Nonnull
    private static Predicate predicate(@Nullable String table, @Nonnull String column, @Nonnull Operator operator, @Nonnull Expression valueExpression) {
        return Expressions.predicate(operator, Expressions.path(valueExpression.getType(), meta(table, column)), valueExpression);
    }

    @Nonnull
    private static PathMetadata meta(@Nullable String table, @Nonnull String column) {
        if (null == table || "".equals(table)) {
            return PathMetadataFactory.forVariable(column);
        } else {
            return PathMetadataFactory.forProperty(Expressions.path(Object.class, table), column);
        }
    }
}
