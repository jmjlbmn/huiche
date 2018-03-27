package org.huiche.core.search;

import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.Expressions;
import org.huiche.core.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author Maning
 */
public class PredicateBuilder {
    private List<Predicate> list;

    public PredicateBuilder() {
        list = new ArrayList<>();
    }

    public <T> PredicateBuilder predicate(Path<T> column, Operator operator, Supplier<T> value) {
        Predicate predicate = null;
        T val = value.get();
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
    public Predicate build(){
        return ExpressionUtils.allOf(list);
    }
}
