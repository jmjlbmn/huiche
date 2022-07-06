package org.huiche.dao.operation;

import com.querydsl.core.types.Predicate;

/**
 * @author Maning
 */
public interface CountOperation<T> {
    /**
     * 查询条数
     *
     * @param conditions 条件
     * @return 条数
     */
    long count(Predicate... conditions);

    /**
     * 查询条数
     *
     * @param query 条件
     * @return 条数
     */
    long count(T query);
}
