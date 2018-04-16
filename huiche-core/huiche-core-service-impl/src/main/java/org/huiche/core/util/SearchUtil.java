package org.huiche.core.util;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Operator;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import lombok.experimental.UtilityClass;
import org.huiche.core.entity.BaseEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 筛选工具类
 *
 * @author Maning
 */
@UtilityClass
public class SearchUtil {
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
