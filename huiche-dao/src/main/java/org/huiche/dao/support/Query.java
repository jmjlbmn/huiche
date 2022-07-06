package org.huiche.dao.support;

import com.querydsl.core.types.Predicate;

/**
 * @author Maning
 */
public interface Query {
    Predicate[] get();
}
