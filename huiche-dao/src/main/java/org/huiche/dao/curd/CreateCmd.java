package org.huiche.dao.curd;

import com.querydsl.sql.dml.DefaultMapper;
import com.querydsl.sql.dml.SQLInsertClause;
import org.huiche.core.exception.HuiCheException;
import org.huiche.dao.provider.CreateHandleProvider;
import org.huiche.dao.provider.PathProvider;
import org.huiche.dao.provider.SqlProvider;
import org.huiche.data.entity.BaseEntity;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.LinkedList;

/**
 * 创建/新增操作
 *
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
        return create(entity, createSetId());
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

    /**
     * 批量插入数据(会忽略ID)
     *
     * @param entityList 实体
     * @return ID
     */
    default long create(@Nonnull Collection<T> entityList) {
        return create(entityList, false);
    }

    /**
     * 批量插入数据(会忽略ID)
     *
     * @param entityList 实体
     * @param fast       是否快速插入,快速插入仅插入需要设置的字段忽略null,但要注意快速插入时,须保证插入的要插入的字段一致,例如要插入name,sex,age字段,批量插入的所有实体都必须设置且只能设置这三个属性的值,不能有null(可以空字符串),不能设置其他属性
     * @return 变更条数
     */
    default long create(@Nonnull Collection<T> entityList, boolean fast) {
        SQLInsertClause insert = sql().insert(root());
        entityList.forEach(t -> {
            beforeCreate(t.setId(null));
            validOnCreate(t);
            if (fast) {
                insert.populate(t).addBatch();
            } else {
                insert.populate(t, DefaultMapper.WITH_NULL_BINDINGS).addBatch();
            }
        });
        long size = insert.getBatchCount();
        if (size > 0) {
            if (createSetId()) {
                LinkedList<Long> ids = new LinkedList<>(insert.executeWithKeys(pk()));
                if (entityList.size() == ids.size()) {
                    entityList.forEach(t -> t.setId(ids.poll()));
                }
            } else {
                insert.execute();
            }
        }
        insert.clear();
        return size;
    }

    /**
     * 批量插入数据,可以携带ID,但没有ID的不会进行ID赋值
     *
     * @param entityList 实体
     * @return 变更条数
     */
    default long createWithId(@Nonnull Collection<T> entityList) {
        SQLInsertClause insert = sql().insert(root());
        entityList.forEach(t -> {
            beforeCreate(t);
            validOnCreate(t);
            insert.populate(t, DefaultMapper.WITH_NULL_BINDINGS).addBatch();
        });
        long size = insert.getBatchCount();
        if (size > 0) {
            insert.execute();
        }
        insert.clear();
        return size;
    }
}
