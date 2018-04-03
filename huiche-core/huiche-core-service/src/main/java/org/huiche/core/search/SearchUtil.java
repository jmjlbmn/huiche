package org.huiche.core.search;

import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.Expressions;
import lombok.experimental.UtilityClass;
import org.huiche.core.annotation.search.SearchColumn;
import org.huiche.core.annotation.search.SearchTable;
import org.huiche.core.consts.Const;
import org.huiche.core.entity.BaseEntity;
import org.huiche.core.util.StringUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * 筛选工具类
 *
 * @author Maning
 */
@UtilityClass
@SuppressWarnings("unchecked")
public class SearchUtil {
    private static final String STAR = "*";
    private static final String LINE = "_";
    private static final String PERCENT = "%";

    /**
     * 通过注解解析search数据
     *
     * @param search 搜索对象
     * @param <S>    搜索对象类型
     * @return 搜索条件
     */
    @Nullable
    public static <S extends Search> Predicate of(@Nullable S search) {
        if (null == search) {
            return null;
        }
        Class<? extends Search> clazz = search.getClass();
        String table = "";
        SearchTable searchTable = clazz.getAnnotation(SearchTable.class);
        if (null != searchTable) {
            table = searchTable.value();
        }
        List<Predicate> list = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            int mod = field.getModifiers();
            try {
                if (Modifier.isPrivate(mod) && !Modifier.isStatic(mod) && !Modifier.isFinal(mod)) {
                    field.setAccessible(true);
                    Object value = field.get(search);
                    if (null != value) {
                        String curTable = table;
                        String column = "";
                        SearchColumn.Op operator = SearchColumn.Op.EQ;
                        boolean not = false;
                        SearchColumn searchColumn = field.getAnnotation(SearchColumn.class);
                        if (null != searchColumn) {
                            if (!"".equals(searchColumn.table())) {
                                curTable = searchColumn.table();
                            }
                            if (!"".equals(searchColumn.value())) {
                                column = searchColumn.value();
                            }
                            if (!SearchColumn.Op.EQ.equals(searchColumn.operator())) {
                                operator = searchColumn.operator();
                            }
                            not = searchColumn.not();
                        }
                        if ("".equals(column)) {
                            column = StringUtil.toDb(field.getName());
                        }
                        Predicate predicate;
                        switch (operator) {
                            case LT:
                                predicate = predicate(curTable, column, Ops.LT, ConstantImpl.create(value));
                                break;
                            case LOE:
                                predicate = predicate(curTable, column, Ops.LOE, ConstantImpl.create(value));
                                break;
                            case GT:
                                predicate = predicate(curTable, column, Ops.GT, ConstantImpl.create(value));
                                break;
                            case GOE:
                                predicate = predicate(curTable, column, Ops.GOE, ConstantImpl.create(value));
                                break;
                            case IS_NULL:
                                predicate = predicate(curTable, column, Ops.IS_NULL, ConstantImpl.create(value));
                                break;
                            case IS_EMPTY:
                                predicate = predicate(curTable, column, Ops.STRING_IS_EMPTY, ConstantImpl.create(value));
                                break;
                            case LIKE:
                                if (value instanceof String) {
                                    String valStr = (String) value;
                                    if (StringUtil.isNotEmpty(valStr)) {
                                        if (valStr.contains(STAR) || valStr.contains(LINE)) {
                                            predicate = predicate(table, column, Ops.LIKE, ConstantImpl.create(valStr.replaceAll("\\*", PERCENT)));
                                        } else {
                                            predicate = predicate(table, column, Ops.STRING_CONTAINS, ConstantImpl.create(value));
                                        }
                                    } else {
                                        predicate = null;
                                    }
                                } else {
                                    predicate = predicate(table, column, Ops.STRING_CONTAINS, ConstantImpl.create(value));
                                }
                                break;
                            case LIKE_IGNORE_CASE:
                                if (value instanceof String) {
                                    String valStr = (String) value;
                                    if (StringUtil.isNotEmpty(valStr)) {
                                        if (valStr.contains(STAR) || valStr.contains(LINE)) {
                                            predicate = predicate(table, column, Ops.LIKE_IC, ConstantImpl.create(valStr.replaceAll("\\*", PERCENT)));
                                        } else {
                                            predicate = predicate(table, column, Ops.STRING_CONTAINS_IC, ConstantImpl.create(value));
                                        }
                                    } else {
                                        predicate = null;
                                    }
                                } else {
                                    predicate = predicate(table, column, Ops.STRING_CONTAINS_IC, ConstantImpl.create(value));
                                }
                                break;
                            case IN:
                                if (value instanceof String) {
                                    List<String> valList = StringUtil.split2List((String) value);
                                    if (valList.size() > 1) {
                                        predicate = predicate(table, column, Ops.IN, ConstantImpl.create(valList));
                                        break;
                                    }
                                }
                                predicate = predicate(table, column, Ops.IN, ConstantImpl.create(value));
                                break;
                            case EQ:
                            default:
                                predicate = predicate(table, column, Ops.EQ, ConstantImpl.create(value));
                                break;
                        }
                        if (null != predicate) {
                            if (!not) {
                                list.add(predicate);
                            } else {
                                list.add(predicate.not());
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (list.isEmpty()) {
            return null;
        } else {
            return ExpressionUtils.allOf(list);
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
                                    predicates.add(predicate(Const.EMPTY_STR, fieldName, Ops.LIKE_IC, ConstantImpl.create(valStr.replaceAll("\\*", PERCENT))));
                                } else {
                                    predicates.add(predicate(Const.EMPTY_STR, fieldName, Ops.STRING_CONTAINS_IC, ConstantImpl.create(value)));
                                }
                            }
                        } else if (value instanceof Number || value instanceof Boolean) {
                            predicates.add(predicate(Const.EMPTY_STR, fieldName, Ops.EQ, ConstantImpl.create(value)));
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
}
