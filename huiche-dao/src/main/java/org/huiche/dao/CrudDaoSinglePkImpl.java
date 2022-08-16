package org.huiche.dao;

import com.querydsl.core.QueryFlag;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.dml.DefaultMapper;
import com.querydsl.sql.dml.SQLInsertClause;
import com.querydsl.sql.dml.SQLUpdateClause;
import org.huiche.dao.support.Query;
import org.huiche.dao.support.Querys;
import org.huiche.exception.HuicheIllegalArgumentException;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Maning
 */
public class CrudDaoSinglePkImpl<T> extends AbstractCrudDao<T> {

    protected final IdInfo idInfo;

    CrudDaoSinglePkImpl(SQLQueryFactory sql, RelationalPath<T> table, IdInfo idInfo) {
        this.sql = sql;
        this.table = table;
        this.idInfo = idInfo;
    }

    @Override
    public <ID extends Serializable> boolean existsById(ID id) {
        return exists(idInfo.idWhere(id));
    }

    @Override
    @Nullable
    public <ID extends Serializable> T getById(ID id, Predicate... conditions) {
        return getOne(Querys.concat(idInfo.idWhere(id), conditions));
    }

    @Override
    @Nullable
    public <ID extends Serializable, Col> Col getColumnById(Expression<Col> column, ID id, Predicate... conditions) {
        return getColumnOne(column, Querys.concat(idInfo.idWhere(id), conditions));
    }

    @Override
    @Nullable
    public <ID extends Serializable> T getColumnsById(Expression<?>[] columns, ID id, Predicate... conditions) {
        return getColumnsOne(columns, Querys.concat(idInfo.idWhere(id), conditions));
    }

    @Override
    @Nullable
    public <ID extends Serializable, DTO> DTO getDtoById(Class<DTO> dtoClass, Expression<?>[] columns, ID id, Predicate... conditions) {
        return getDtoOne(dtoClass, columns, Querys.concat(idInfo.idWhere(id), conditions));
    }

    @Override

    public <ID extends Serializable> List<T> listByIds(Collection<ID> ids) {
        return list(Query.of(idInfo.idsWhere(ids)));
    }

    @Override

    public <ID extends Serializable> List<T> listColumnsByIds(Expression<?>[] columns, Collection<ID> ids) {
        return listColumns(columns, idInfo.idsWhere(ids));
    }

    @Override

    public <ID extends Serializable, Col> List<Col> listColumnByIds(Path<Col> column, Collection<ID> ids) {
        return listColumn(column, idInfo.idsWhere(ids));
    }

    @Override

    public <ID extends Serializable, DTO> List<DTO> listDtoByIds(Class<DTO> dtoClass, Expression<?>[] columns, Collection<ID> ids) {
        return listDto(dtoClass, columns, idInfo.idsWhere(ids));
    }

    @Override
    public <ID extends Serializable> long deleteById(ID id) {
        return delete(idInfo.idWhere(id));
    }

    @Override
    public <ID extends Serializable> long deleteByIds(Collection<ID> ids) {
        return delete(idInfo.idsWhere(ids));
    }

    @Override
    public <ID extends Serializable> long updateById(@Nullable T entityUpdate, @Nullable Consumer<SQLUpdateClause> setter, ID id) {
        return update(entityUpdate, setter, idInfo.idWhere(id));
    }

    @Override
    public <E extends T> E create(E entity) {
        beforeCreate(entity);
        SQLInsertClause dml = sql.insert(table);
        boolean autoIncrement = idInfo.handleId(entity);
        dml.populate(entity);
        if (autoIncrement) {
            idInfo.setIdVal(entity, dml.executeWithKey(idInfo.getPath()));
        } else {
            dml.execute();
        }
        return entity;
    }

    @Override
    public <E extends T> E replace(E entity) {
        beforeCreate(entity);
        if (idInfo.getIdVal(entity) == null) {
            throw new HuicheIllegalArgumentException("replace operation need primary key has a value");
        }
        sql.insert(table).addFlag(QueryFlag.Position.START_OVERRIDE, "REPLACE INTO ").populate(entity).execute();
        return entity;
    }


    @Override
    public <E extends T> E save(E entity) {
        Object val = idInfo.getIdVal(entity);
        if (val == null) {
            return create(entity);
        } else {
            update(entity, idInfo.idWhere(val));
            return entity;
        }
    }

    @Override
    public <E extends T> long createBatch(Collection<E> entityList) {
        SQLInsertClause dmlHasId = sql.insert(table);
        SQLInsertClause dmlHasNoId = sql.insert(table);
        List<T> hasNoIdList = new ArrayList<>(entityList.size());
        for (T entity : entityList) {
            beforeCreate(entity);
            if (idInfo.handleId(entity)) {
                dmlHasNoId.populate(entity, DefaultMapper.WITH_NULL_BINDINGS).addBatch();
                hasNoIdList.add(entity);
            } else {
                dmlHasId.populate(entity, DefaultMapper.WITH_NULL_BINDINGS).addBatch();
            }
        }
        long result = 0;
        if (idInfo.isAutoIncrement() && !dmlHasNoId.isEmpty()) {
            result += autoIncrementIdsExecuteAndSet(dmlHasNoId, idInfo, hasNoIdList);
        }
        if (!dmlHasId.isEmpty()) {
            result += dmlHasId.execute();
        }
        return result;
    }


}
