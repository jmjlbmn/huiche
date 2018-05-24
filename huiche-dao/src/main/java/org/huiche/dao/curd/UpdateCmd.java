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
import javax.annotation.Nullable;

/**
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
     * 根据条件更新实体
     *
     * @param entity    要更新的内容
     * @param predicate 条件
     * @return 变更条数
     */
    default long update(@Nonnull T entity, @Nullable Predicate... predicate) {
        Long id = entity.getId();
        Assert.notNull("更新时ID不能为空", id);
        Assert.ok("更新时条件不能为空", null != predicate && predicate.length > 0);
        validRegular(entity);
        // 强制不更新ID
        entity.setId(null);
        return sql().update(root()).populate(entity).where(pk().eq(id)).where(predicate).execute();
    }
}
