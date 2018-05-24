package org.huiche.dao.curd;

import com.querydsl.core.types.Predicate;
import com.querydsl.sql.dml.SQLUpdateClause;
import org.huiche.core.util.Assert;
import org.huiche.dao.provider.PathProvider;
import org.huiche.dao.provider.SqlProvider;
import org.huiche.dao.provider.UpdateHandleProvider;
import org.huiche.data.entity.BaseEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

/**
 * @author Maning
 */
public interface UpdatesCmd<T extends BaseEntity<T>> extends PathProvider<T>, SqlProvider, UpdateHandleProvider<T> {
    /**
     * 根据ID更新实体列表,必须设置ID,不设置ID将跳过
     *
     * @param entityList 实体列表
     * @return 变动条数
     */
    default long updates(@Nonnull Collection<T> entityList) {
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

    /**
     * 根据条件更新实体
     *
     * @param entity    要更新的内容
     * @param predicate 条件
     * @return 变更条数
     */
    default long updates(@Nonnull T entity, @Nullable Predicate... predicate) {
        Assert.ok("更新时条件不能为空", null != predicate && predicate.length > 0);
        // 强制不更新ID
        entity.setId(null);
        validRegular(entity);
        return sql().update(root()).populate(entity).where(predicate).execute();
    }
}
