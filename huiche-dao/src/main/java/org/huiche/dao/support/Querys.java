package org.huiche.dao.support;

import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.sql.RelationalPath;
import org.huiche.support.ReflectUtil;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.lang.reflect.Field;
import java.time.*;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Maning
 */
public class Querys {
    public static Expression<?>[] columns(Expression<?>... cols) {
        return cols;
    }

    public static OrderSpecifier<?>[] orders(OrderSpecifier<?>... orders) {
        return orders;
    }

    public static <T> Predicate[] ofEntity(T entity4Query, RelationalPath<T> table) {
        List<Predicate> pds = new ArrayList<>();
        try {
            Map<String, Path<?>> columnsMap = table.getColumns().stream().collect(Collectors.toMap(t -> t.getMetadata().getName(), Function.identity()));
            for (Field field : ReflectUtil.scanNormalFields(entity4Query.getClass())) {
                try {
                    field.setAccessible(true);
                } catch (SecurityException ignored) {
                }
                Object val = field.get(entity4Query);
                String fieldName = field.getName();
                if (val != null && columnsMap.containsKey(fieldName)) {
                    String sv = String.valueOf(val);
                    if (val instanceof CharSequence) {
                        pds.add(Expressions.stringPath(table, fieldName).contains(sv));
                    } else if (val instanceof Number) {
                        pds.add(Expressions.numberPath(Double.class, table, fieldName).eq(Double.valueOf(sv)));
                    } else if (val instanceof Boolean) {
                        pds.add(Expressions.booleanPath(table, fieldName).eq(Boolean.valueOf(sv)));
                    } else if (field.getType().isEnum() ||
                            val instanceof Date || val instanceof LocalDate || val instanceof LocalTime || val instanceof LocalDateTime ||
                            val instanceof OffsetTime || val instanceof OffsetDateTime || val instanceof ZonedDateTime) {
                        pds.add(Expressions.stringPath(table, fieldName).eq(sv));
                    }
                }
            }
            return pds.toArray(new Predicate[0]);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 如果ok为true,则返回条件,否则返回null
     *
     * @param ok        是否添加条件
     * @param predicate 条件提供者
     * @return 条件
     */
    @Nullable
    public static Predicate predicate(boolean ok, @NonNull Supplier<Predicate> predicate) {
        if (ok) {
            return predicate.get();
        }
        return null;
    }

    /**
     * 如果val不是空,则返回对值进行的匹配条件,否则返回null
     *
     * @param op  操作方法
     * @param val 值
     * @param <T> 值类型
     * @return 条件
     */
    @Nullable
    public static <T> Predicate predicate(@Nullable T val, @NonNull Function<T, Predicate> op) {
        return predicate(val != null, () -> op.apply(val));
    }

    /**
     * 如果val不是空,则返回条件,否则返回null
     *
     * @param predicate 条件
     * @param val       值
     * @param <T>       值类型
     * @return 条件
     */
    @Nullable
    public static <T> Predicate predicate(@Nullable T val, @NonNull Supplier<Predicate> predicate) {
        if (val != null) {
            return predicate.get();
        }
        return null;
    }

    /**
     * 条件提供者
     *
     * @param predicate 条件
     * @param <T>       值类型
     * @return 条件
     */
    @Nullable
    public static <T> Predicate predicate(@NonNull Supplier<Predicate> predicate) {
        return predicate.get();
    }

    /**
     * 关键字
     *
     * @param keyword 关键字
     * @param cols    关键字列
     * @return 条件
     */
    @Nullable
    public static Predicate keyword(@Nullable String keyword, @NonNull StringExpression... cols) {
        if (keyword == null || cols.length == 0) {
            return null;
        }
        keyword = keyword.trim();
        if (keyword.length() == 0) {
            return null;
        }
        List<Predicate> list = new ArrayList<>(cols.length);
        for (StringExpression col : cols) {
            list.add(col.contains(keyword));
        }
        return ExpressionUtils.anyOf(list);
    }

    /**
     * 关键字
     *
     * @param keyword 关键字
     * @param cols    关键字列
     * @return 条件
     * @separator 分隔符
     */
    @Nullable
    public static Predicate keywordSplit(@Nullable String keyword, @NonNull String separator, @NonNull StringExpression... cols) {
        if (keyword == null || keyword.trim().length() == 0 || cols.length == 0) {
            return null;
        }
        String[] keywords = keyword.split(separator);
        if (keywords.length == 0) {
            return null;
        }
        List<Predicate> list = new ArrayList<>(cols.length);
        for (StringExpression col : cols) {
            for (String word : keywords) {
                word = word.trim();
                if (word.length() > 0) {
                    list.add(col.contains(word));
                }
            }
        }
        return ExpressionUtils.anyOf(list);
    }

    /**
     * 关键字
     *
     * @param keyword 关键字
     * @param cols    关键字列
     * @return 条件
     */
    @Nullable
    public static Predicate keywordSplit(@Nullable String keyword, @NonNull StringExpression... cols) {
        return keywordSplit(keyword, " ", cols);
    }

    /**
     * 用and组合多个条件,等同predicates
     *
     * @param predicate 多个条件
     * @return 最终条件
     */
    @Nullable
    public static Predicate and(@NonNull Predicate... predicate) {
        return ExpressionUtils.allOf(predicate);
    }

    /**
     * or
     *
     * @param predicate 条件
     * @return 最终条件
     */
    @Nullable
    public static Predicate or(@NonNull Predicate... predicate) {
        return ExpressionUtils.anyOf(predicate);
    }

    /**
     * 扩展增加dto继承
     *
     * @param beanPath 实体类的查询类
     * @param columns  扩展的列
     * @param <T>      实体类
     * @return 所有查询的列
     */
    @NonNull
    public static <T> Expression<?>[] extendColumn(@NonNull RelationalPath<T> beanPath, @NonNull Expression<?>... columns) {
        List<Expression<?>> list = new ArrayList<>();
        list.addAll(beanPath.getColumns());
        list.addAll(Arrays.asList(columns));
        return list.toArray(new Expression[0]);
    }

    /**
     * 扩展增加dto继承
     *
     * @param beanPath 实体类的查询类
     * @param columns  排除的列
     * @param <T>      实体类
     * @return 所有查询的列
     */
    @NonNull
    public static <T> Expression<?>[] excludeColumn(@NonNull RelationalPath<T> beanPath, @NonNull Expression<?>... columns) {
        List<Path<?>> pathColumns = beanPath.getColumns();
        if (columns.length > 0) {
            List<Expression<?>> excludeList = Arrays.asList(columns);
            pathColumns.removeIf(excludeList::contains);
        }
        return pathColumns.toArray(new Expression[0]);
    }

    /**
     * 获取继承bean的dto
     *
     * @param dtoClass dto的class
     * @param beanPath 实体类的查询类
     * @param columns  扩展的列
     * @param <T>      实体类
     * @param <DTO>    dto的类
     * @return dto
     */
    @NonNull
    public static <DTO extends T, T> QBean<DTO> extendBean(@NonNull Class<DTO> dtoClass, @NonNull RelationalPath<T> beanPath, @NonNull Expression<?>... columns) {
        return Projections.fields(dtoClass, extendColumn(beanPath, columns));
    }
}
