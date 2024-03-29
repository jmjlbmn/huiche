package org.huiche.dao.operation;

import com.querydsl.core.types.Predicate;
import com.querydsl.sql.dml.SQLUpdateClause;
import org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * @author Maning
 */
public interface UpdateOperation<T> {
    /**
     * 通过ID更新实体
     *
     * @param entityUpdate 实体
     * @param id           ID
     * @param <ID>         ID类型
     * @return 更新条数
     */
    default <ID extends Serializable> long updateById(T entityUpdate, ID id) {
        return updateById(entityUpdate, null, id);
    }

    /**
     * 通过ID更新实体
     *
     * @param entityUpdate 实体
     * @param setter       自定义更新
     * @param id           ID
     * @param <ID>         ID类型
     * @return 更新条数
     */
    <ID extends Serializable> long updateById(@Nullable T entityUpdate, @Nullable Consumer<SQLUpdateClause> setter, ID id);

    /**
     * 通过ID更新实体
     *
     * @param setter 自定义更新
     * @param id     ID
     * @param <ID>   ID类型
     * @return 更新条数
     */
    default <ID extends Serializable> long updateById(Consumer<SQLUpdateClause> setter, ID id) {
        return updateById(null, setter, id);
    }

    /**
     * 通过条件更新实体
     *
     * @param entityUpdate 实体
     * @param conditions   条件
     * @return 更新条数
     */
    default long update(T entityUpdate, Predicate... conditions) {
        return update(entityUpdate, null, conditions);
    }

    /**
     * 通过条件更新实体
     *
     * @param entityUpdate 实体
     * @param setter       自定义更新
     * @param conditions   条件
     * @return 更新条数
     */
    long update(@Nullable T entityUpdate, @Nullable Consumer<SQLUpdateClause> setter, Predicate... conditions);

    /**
     * 通过条件更新实体
     *
     * @param setter     自定义更新
     * @param conditions 条件
     * @return 更新条数
     */
    default long update(Consumer<SQLUpdateClause> setter, Predicate... conditions) {
        return update(null, setter, conditions);
    }

    /**
     * 通过条件更新实体 只更新一行,否则抛异常
     *
     * @param entityUpdate 实体
     * @param setter       自定义更新
     * @param conditions   条件
     */
    default void updateOneRow(@Nullable T entityUpdate, @Nullable Consumer<SQLUpdateClause> setter, Predicate... conditions) {
        long updateCount = update(entityUpdate, setter, conditions);
        if (updateCount != 1) {
            throw new JdbcUpdateAffectedIncorrectNumberOfRowsException("huiche-dao-update", 1, (int) updateCount);
        }
    }

    /**
     * 通过条件更新实体 只更新一行,否则抛异常
     *
     * @param entityUpdate 实体
     * @param conditions   条件
     */
    default void updateOneRow(T entityUpdate, Predicate... conditions) {
        updateOneRow(entityUpdate, null, conditions);
    }

    /**
     * 通过条件更新实体 只更新一行,否则抛异常
     *
     * @param setter     自定义更新
     * @param conditions 条件
     */
    default void updateOneRow(Consumer<SQLUpdateClause> setter, Predicate... conditions) {
        updateOneRow(null, setter, conditions);
    }
}
