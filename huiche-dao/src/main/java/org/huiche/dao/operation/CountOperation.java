package org.huiche.dao.operation;

import com.querydsl.core.types.Predicate;
import org.jetbrains.annotations.NotNull;

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
    long count(@NotNull Predicate... conditions);
}
