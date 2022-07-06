package org.huiche.dao.operation;

import com.querydsl.core.types.Predicate;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author Maning
 */
public interface DeleteOperation<T> {

    /**
     * 通过ID删除,只支持单主键
     *
     * @param id   ID
     * @param <ID> ID类型
     * @return 删除条数
     */
    <ID extends Serializable> long deleteById(ID id);

    /**
     * 通过ID列表删除,只支持单主键
     *
     * @param ids  ID列表
     * @param <ID> ID类型
     * @return 删除条数
     */
    <ID extends Serializable> long deleteByIds(Collection<ID> ids);

    /**
     * 通过条件删除
     *
     * @param conditions 条件
     * @return 删除条数
     */
    long delete(Predicate... conditions);
}
