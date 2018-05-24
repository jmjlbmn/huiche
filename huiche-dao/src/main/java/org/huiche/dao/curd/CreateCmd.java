package org.huiche.dao.curd;

import org.huiche.core.exception.HuiCheException;
import org.huiche.dao.provider.CreateHandleProvider;
import org.huiche.dao.provider.PathProvider;
import org.huiche.dao.provider.SqlProvider;
import org.huiche.data.entity.BaseEntity;

import javax.annotation.Nonnull;

/**
 * @author Maning
 */
public interface CreateCmd<T extends BaseEntity<T>> extends PathProvider<T>, SqlProvider, CreateHandleProvider<T> {
    /**
     * 新增数据并设置ID(默认,可通过复写doSetId改变)
     *
     * @param entity 实体
     * @return ID
     */
    default long create(@Nonnull T entity) {
        return create(entity, doSetId());
    }

    /**
     * 新增数据
     *
     * @param entity 实体
     * @param setId  是否设置ID
     * @return ID
     */
    default long create(@Nonnull T entity, boolean setId) {
        beforeCreate(entity);
        validOnCreate(entity);
        Long id = entity.getId();
        if (null == id) {
            id = sql().insert(root())
                    .populate(entity)
                    .executeWithKey(pk());
        } else {
            sql().insert(root())
                    .populate(entity)
                    .execute();
        }
        if (null == id) {
            throw new HuiCheException("新增数据失败");
        } else {
            if (setId) {
                entity.setId(id);
            }
            return id;
        }
    }
}
