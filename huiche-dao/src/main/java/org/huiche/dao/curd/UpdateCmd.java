package org.huiche.dao.curd;

import com.querydsl.core.types.Predicate;
import com.querydsl.sql.dml.DefaultMapper;
import com.querydsl.sql.dml.SQLUpdateClause;
import org.huiche.core.exception.HuiCheError;
import org.huiche.core.util.Assert;
import org.huiche.dao.provider.PathProvider;
import org.huiche.dao.provider.SqlProvider;
import org.huiche.dao.provider.UpdateHandleProvider;
import org.huiche.data.entity.BaseEntity;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.function.Consumer;

/**
 * 更新操作
 *
 * @author Maning
 */
public interface UpdateCmd<T extends BaseEntity<T>> extends PathProvider<T>, SqlProvider, UpdateHandleProvider<T> {
    /**
     * 根据ID更新实体
     *
     * @param entity 实体
     * @return 变更条数
     */
    default long update(@Nonnull T entity) {
        return update(entity, true);
    }

    /**
     * 根据ID更新实体
     *
     * @param entity     实体
     * @param ignoreNull 是否忽略空属性,忽略时仅对有值的字段进行更新,否则全部进行更新
     * @return 变更条数
     */
    default long update(@Nonnull T entity, boolean ignoreNull) {
        Assert.notNull(HuiCheError.UPDATE_MUST_HAVE_ID, entity.getId());
        beforeUpdate(entity);
        validRegular(entity);
        SQLUpdateClause update = sql().update(root());
        if (ignoreNull) {
            return update.populate(entity).where(pk().eq(entity.getId())).execute();
        } else {
            return update.populate(entity, DefaultMapper.WITH_NULL_BINDINGS).where(pk().eq(entity.getId())).execute();
        }
    }

    /**
     * 根据条件更新实体,完全根据条件更新,忽略ID
     *
     * @param entity    要更新的内容
     * @param predicate 条件
     * @return 变更条数
     */
    default long update(@Nonnull T entity, Predicate... predicate) {
        beforeUpdate(entity);
        validRegular(entity);
        if (entity.getId() == null) {
            return sql().update(root()).populate(entity).where(predicate).execute();
        } else {
            return sql().update(root()).populate(entity).where(pk().eq(entity.getId())).where(predicate).execute();
        }
    }

    /**
     * 根据条件更新,自行set更新内容
     *
     * @param entity 实体类
     * @param setter update
     * @return 更新条数
     */
    default long update(@Nonnull T entity, Consumer<SQLUpdateClause> setter) {
        Assert.notNull(HuiCheError.UPDATE_MUST_HAVE_ID, entity.getId());
        beforeUpdate(entity);
        validRegular(entity);
        SQLUpdateClause update = sql().update(root()).populate(entity);
        setter.accept(update);
        return update.where(pk().eq(entity.getId())).execute();
    }

    /**
     * 根据条件更新,自行set更新内容,注意该方法不会更新修改时间,也不进行校验
     *
     * @param setter    update
     * @param predicate 条件
     * @return 更新条数
     */
    default long update(Consumer<SQLUpdateClause> setter, Predicate... predicate) {
        Assert.ok("更新时条件不能为空", null != predicate && predicate.length > 0);
        SQLUpdateClause update = sql().update(root());
        setter.accept(update);
        return update.where(predicate).execute();
    }

    /**
     * 根据条件更新,自行set更新内容,注意该方法通过setter进行的字段更新不会进行校验
     *
     * @param entity    实体类
     * @param setter    update
     * @param predicate 条件
     * @return 更新条数
     */
    default long update(@Nonnull T entity, Consumer<SQLUpdateClause> setter, Predicate... predicate) {
        beforeUpdate(entity);
        validRegular(entity);
        SQLUpdateClause update = sql().update(root()).populate(entity);
        setter.accept(update);
        if (entity.getId() == null) {
            return update.where(predicate).execute();
        } else {
            return update.where(pk().eq(entity.getId())).where(predicate).execute();
        }
    }

    /**
     * 根据ID更新实体列表,实体类必须设置ID,不设置ID将跳过
     *
     * @param entityList 实体列表
     * @return 变动条数
     */
    default long update(@Nonnull Collection<T> entityList) {
        SQLUpdateClause update = sql().update(root());
        for (T entity : entityList) {
            Long id = entity.getId();
            if (null != id) {
                beforeUpdate(entity);
                validRegular(entity);
                update.populate(entity.setId(null)).where(pk().eq(id)).addBatch();
            }
        }
        if (!update.isEmpty()) {
            return update.execute();
        } else {
            update.clear();
            return 0;
        }
    }
}
