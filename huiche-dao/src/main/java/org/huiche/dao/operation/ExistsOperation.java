package org.huiche.dao.operation;

import com.querydsl.core.types.Predicate;

import java.io.Serializable;

/**
 * @author Maning
 */
public interface ExistsOperation<T> {

    /**
     * 通过ID判断是否存在
     *
     * @param id   ID
     * @param <ID> ID类型
     * @return 是否存在
     */
    <ID extends Serializable> boolean existsById(ID id);

    /**
     * 通过条件判断是否存在
     *
     * @param conditions 条件
     * @return 是否存在
     */
    boolean exists(Predicate... conditions);
}
