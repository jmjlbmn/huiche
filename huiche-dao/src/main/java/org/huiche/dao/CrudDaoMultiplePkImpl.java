package org.huiche.dao;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.dml.DefaultMapper;
import com.querydsl.sql.dml.SQLInsertClause;
import com.querydsl.sql.dml.SQLUpdateClause;
import com.querydsl.sql.mysql.MySQLReplaceClause;
import org.huiche.exception.HuicheIllegalArgumentException;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.TypeMismatchDataAccessException;

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
    @NotNull
    public T create(@NotNull T entity) {
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
    @NotNull
    public T createOrReplace(@NotNull T entity) {
        beforeCreate(entity);
        if (autoIncrementId != null && autoIncrementId.getIdVal(entity) == null) {
            throw new HuicheIllegalArgumentException("replace operation need primary key has a value");
        }
        otherIdInfoList.forEach(id -> id.handleId(entity));
        new MySQLReplaceClause(sql.getConnection(), sql.getConfiguration(), table).populate(entity).execute();
        return entity;
    }

    @Override
    public long createBatch(@NotNull Collection<T> entityList) {
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
    public <ID extends Serializable> boolean existsById(@NotNull ID id) {
        throw EXCEPTION;
    }

    @Override
    public <ID extends Serializable> T getById(@NotNull ID id) {
        throw EXCEPTION;
    }

    @Override
    public <ID extends Serializable, Col> Col getColumnById(@NotNull Expression<Col> column, @NotNull ID id) {
        throw EXCEPTION;
    }

    @Override
    public <ID extends Serializable> T getColumnsById(Expression<?> @NotNull [] columns, @NotNull ID id) {
        throw EXCEPTION;
    }

    @Override
    public <ID extends Serializable, DTO> DTO getDtoById(@NotNull Class<DTO> dtoClass, Expression<?> @NotNull [] columns, @NotNull ID id) {
        throw EXCEPTION;
    }

    @Override
    @NotNull
    public <ID extends Serializable> List<T> listByIds(@NotNull Collection<ID> ids) {
        throw EXCEPTION;
    }

    @Override
    @NotNull
    public <ID extends Serializable> List<T> listColumnsByIds(@NotNull Expression<?>[] columns, @NotNull Collection<ID> ids) {
        throw EXCEPTION;
    }

    @Override
    @NotNull
    public <ID extends Serializable, Col> List<Col> listColumnByIds(@NotNull Path<Col> column, @NotNull Collection<ID> ids) {
        throw EXCEPTION;
    }

    @Override
    @NotNull
    public <ID extends Serializable, DTO> List<DTO> listDtoByIds(@NotNull Class<DTO> dtoClass, Expression<?> @NotNull [] columns, @NotNull Collection<ID> ids) {
        throw EXCEPTION;
    }

    @Override
    public <ID extends Serializable> long deleteById(@NotNull ID id) {
        throw EXCEPTION;
    }

    @Override
    public <ID extends Serializable> long deleteByIds(@NotNull Collection<ID> ids) {
        throw EXCEPTION;
    }

    @Override
    public <ID extends Serializable> long updateById(T entityUpdate, Consumer<SQLUpdateClause> setter, @NotNull ID id) {
        throw EXCEPTION;
    }
}
