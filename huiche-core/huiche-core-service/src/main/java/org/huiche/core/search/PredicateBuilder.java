package org.huiche.core.search;

import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.Expressions;
import org.huiche.core.util.StringUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询条件构造器
 *
 * @author Maning
 */
public class PredicateBuilder {
    private List<Predicate> list;

    public PredicateBuilder() {
        list = new ArrayList<>();
    }

    /**
     * 添加条件
     *
     * @param column   字段
     * @param operator 操作,比较符
     * @param val      值
     * @param <T>      值的类型
     * @return 构造器
     */
    @Nonnull
    public <T> PredicateBuilder predicate(@Nonnull Path<T> column,@Nonnull Operator operator,@Nullable T val) {
        Predicate predicate = null;
        if (null != val) {
            if (val instanceof String) {
                String valStr = (String) val;
                if (StringUtil.isNotEmpty(valStr)) {
                    predicate = Expressions.predicate(operator, column, ConstantImpl.create(val));
                }
            } else {
                predicate = Expressions.predicate(operator, column, ConstantImpl.create(val));
            }
        }
        list.add(predicate);
        return this;
    }

    /**
     * 添加条件
     *
     * @param predicate 条件
     * @return 构造器
     */
    @Nonnull
    public PredicateBuilder predicate(@Nullable Predicate predicate) {
        list.add(predicate);
        return this;
    }

    /**
     * 构造筛选条件
     *
     * @return 筛选条件
     */
    @Nullable
    public Predicate build() {
        return ExpressionUtils.allOf(list);
    }
}
