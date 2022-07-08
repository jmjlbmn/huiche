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
import org.huiche.exception.HuicheIllegalArgumentException;
import org.springframework.dao.TypeMismatchDataAccessException;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Maning
 */
public class CrudDaoMultiplePkImpl<T> extends AbstractCrudDao<T> {
    private static final RuntimeException EXCEPTION = new TypeMismatchDataAccessException("this operation need table has one and only primary key");
    private final IdInfo autoIncrementId;
    private final List<IdInfo> otherIdInfoList = new ArrayList<>();
    private final RelationalPath<T> table;


    CrudDaoMultiplePkImpl(SQLQueryFactory sql, RelationalPath<T> table, List<IdInfo> idInfoList) {
        this.sql = sql;
        this.table = table;
        IdInfo autoId = null;
        for (IdInfo idInfo : idInfoList) {
            if (idInfo.isAutoIncrement()) {
                if (autoId == null) {
                    autoId = idInfo;
                } else {
                    throw EXCEPTION;
                }
            } else {
                otherIdInfoList.add(idInfo);
            }
        }
        this.autoIncrementId = autoId;
    }

    @Override
    public <ID extends Serializable> boolean existsById(ID id) {
        throw EXCEPTION;
    }    @Override
    public <E extends T> E create(E entity) {
        beforeCreate(entity);
        SQLInsertClause dml = sql.insert(table);
        boolean autoIncrement = autoIncrementId != null && autoIncrementId.handleId(entity);
        otherIdInfoList.forEach(id -> id.handleId(entity));
        dml.populate(entity);
        if (autoIncrement) {
            autoIncrementId.setIdVal(entity, dml.executeWithKey(autoIncrementId.getPath()));
        } else {
            dml.execute();
        }
        return entity;
    }

    @Override
    public <ID extends Serializable> T getById(ID id) {
        throw EXCEPTION;
    }    @Override

    public <E extends T> E replace(E entity) {
        beforeCreate(entity);
        if (autoIncrementId != null && autoIncrementId.getIdVal(entity) == null) {
            throw new HuicheIllegalArgumentException("replace operation need primary key has a value");
        }
        otherIdInfoList.forEach(id -> id.handleId(entity));
        sql.insert(table).addFlag(QueryFlag.Position.START_OVERRIDE, "REPLACE INTO ").populate(entity).execute();
        return entity;
    }

    @Override
    public <ID extends Serializable, Col> Col getColumnById(Expression<Col> column, ID id) {
        throw EXCEPTION;
    }    @Override
    public <E extends T> E save(E entity) {
        boolean isUpdate = true;
        List<Predicate> idWhere = new ArrayList<>();
        if (autoIncrementId != null) {
            Object val = autoIncrementId.getIdVal(entity);
            if (val == null) {
                isUpdate = false;
            } else {
                idWhere.add(autoIncrementId.idWhere(val));
            }
        }
        for (IdInfo idInfo : otherIdInfoList) {
            Object val = idInfo.getIdVal(entity);
            if (val == null) {
                isUpdate = false;
                break;
            }
            idWhere.add(idInfo.idWhere(val));
        }
        if (isUpdate) {
            update(entity, idWhere.toArray(new Predicate[0]));
            return entity;
        } else {
            return create(entity);
        }
    }

    @Override
    public <ID extends Serializable> T getColumnsById(Expression<?>[] columns, ID id) {
        throw EXCEPTION;
    }    @Override
    public <E extends T> long createBatch(Collection<E> entityList) {
        SQLInsertClause dmlHasNoId = sql.insert(table);
        SQLInsertClause dmlHasId = sql.insert(table);
        List<T> hasNoIdList = new ArrayList<>(entityList.size());
        for (T entity : entityList) {
            beforeCreate(entity);
            if (autoIncrementId != null && autoIncrementId.getIdVal(entity) == null) {
                dmlHasNoId.populate(entity, DefaultMapper.WITH_NULL_BINDINGS).addBatch();
                hasNoIdList.add(entity);
                continue;
            }
            otherIdInfoList.forEach(id -> id.handleId(entity));
            dmlHasId.populate(entity, DefaultMapper.WITH_NULL_BINDINGS).addBatch();
        }
        long result = 0;
        if (autoIncrementId != null && !dmlHasNoId.isEmpty()) {
            result += autoIncrementIdsExecuteAndSet(dmlHasNoId, autoIncrementId, hasNoIdList);
        }
        if (!dmlHasId.isEmpty()) {
            result += dmlHasId.execute();
        }
        return result;
    }

    @Override
    public <ID extends Serializable, DTO> DTO getDtoById(Class<DTO> dtoClass, Expression<?>[] columns, ID id) {
        throw EXCEPTION;
    }

    @Override

    public <ID extends Serializable> List<T> listByIds(Collection<ID> ids) {
        throw EXCEPTION;
    }

    @Override

    public <ID extends Serializable> List<T> listColumnsByIds(Expression<?>[] columns, Collection<ID> ids) {
        throw EXCEPTION;
    }

    @Override

    public <ID extends Serializable, Col> List<Col> listColumnByIds(Path<Col> column, Collection<ID> ids) {
        throw EXCEPTION;
    }

    @Override

    public <ID extends Serializable, DTO> List<DTO> listDtoByIds(Class<DTO> dtoClass, Expression<?>[] columns, Collection<ID> ids) {
        throw EXCEPTION;
    }

    @Override
    public <ID extends Serializable> long deleteById(ID id) {
        throw EXCEPTION;
    }

    @Override
    public <ID extends Serializable> long deleteByIds(Collection<ID> ids) {
        throw EXCEPTION;
    }

    @Override
    public <ID extends Serializable> long updateById(@Nullable T entityUpdate, @Nullable Consumer<SQLUpdateClause> setter, ID id) {
        throw EXCEPTION;
    }








}
