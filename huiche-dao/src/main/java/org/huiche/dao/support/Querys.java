package org.huiche.dao.support;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.sql.RelationalPath;
import org.huiche.support.ReflectUtil;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
            for (Field field : ReflectUtil.scanNormalFields(entity4Query.getClass())) {
                field.setAccessible(true);
                Object val = field.get(entity4Query);
                if (val != null) {
                    String sv = String.valueOf(val);
                    if (val instanceof CharSequence) {
                        pds.add(Expressions.stringPath(table, field.getName()).contains(sv));
                    } else if (val instanceof Number) {
                        pds.add(Expressions.numberPath(Double.class, table, field.getName()).eq(Double.valueOf(sv)));
                    } else if (val instanceof Boolean) {
                        pds.add(Expressions.booleanPath(table, field.getName()).eq(Boolean.valueOf(sv)));
                    } else if (field.getType().isEnum() ||
                            val instanceof Date || val instanceof LocalDate || val instanceof LocalTime || val instanceof LocalDateTime ||
                            val instanceof OffsetTime || val instanceof OffsetDateTime || val instanceof ZonedDateTime) {
                        pds.add(Expressions.stringPath(table, field.getName()).eq(sv));
                    }
                }
            }
            return pds.toArray(new Predicate[0]);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
