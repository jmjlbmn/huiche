package org.huiche.dao.provider;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.sql.RelationalPath;

/**
 * @author Maning
 */
public interface PathProvider<T> {
    OrderSpecifier[] defaultMultiOrder();
    RelationalPath<T> root();
    NumberPath<Long> pk();
}
