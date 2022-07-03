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
import org.jetbrains.annotations.Nullable;

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
    public <ID extends Serializable> boolean existsById(@NotNull ID id) {
        return exists(idInfo.idWhere(id));
    }

    @Override
    @Nullable
    public <ID extends Serializable> T getById(@NotNull ID id) {
        return getOne(idInfo.idWhere(id));
    }

    @Override
    @Nullable
    public <ID extends Serializable, Col> Col getColumnById(@NotNull Expression<Col> column, @NotNull ID id) {
        return getColumnOne(column, idInfo.idWhere(id));
    }

    @Override
    @Nullable
    public <ID extends Serializable> T getColumnsById(Expression<?> @NotNull [] columns, @NotNull ID id) {
        return getColumnsOne(columns, idInfo.idWhere(id));
    }

    @Override
    @Nullable
    public <ID extends Serializable, DTO> DTO getDtoById(@NotNull Class<DTO> dtoClass, Expression<?> @NotNull [] columns, @NotNull ID id) {
        return getDtoOne(dtoClass, columns, idInfo.idWhere(id));
    }

    @Override
    @NotNull
    public <ID extends Serializable> List<T> listByIds(@NotNull Collection<ID> ids) {
        return list(Q.of(idInfo.idsWhere(ids)));
    }

    @Override
    @NotNull
    public <ID extends Serializable> List<T> listColumnsByIds(Expression<?> @NotNull [] columns, @NotNull Collection<ID> ids) {
        return listColumns(columns, idInfo.idsWhere(ids));
    }

    @Override
    @NotNull
    public <ID extends Serializable, Col> List<Col> listColumnByIds(@NotNull Path<Col> column, @NotNull Collection<ID> ids) {
        return listColumn(column, idInfo.idsWhere(ids));
    }

    @Override
    @NotNull
    public <ID extends Serializable, DTO> List<DTO> listDtoByIds(@NotNull Class<DTO> dtoClass, Expression<?> @NotNull [] columns, @NotNull Collection<ID> ids) {
        return listDto(dtoClass, columns, idInfo.idsWhere(ids));
    }

    @Override
    @NotNull
    public T create(@NotNull T entity) {
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
    @NotNull
    public T createOrReplace(@NotNull T entity) {
        beforeCreate(entity);
        if (idInfo.getIdVal(entity) == null) {
            throw new HuicheIllegalArgumentException("replace operation need primary key has a value");
        }
        new MySQLReplaceClause(sql.getConnection(), sql.getConfiguration(), table).populate(entity).execute();
        return entity;
    }

    @Override
    public long createBatch(@NotNull Collection<T> entityList) {
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

    @Override
    public <ID extends Serializable> long deleteById(@NotNull ID id) {
        return delete(idInfo.idWhere(id));
    }

    @Override
    public <ID extends Serializable> long deleteByIds(@NotNull Collection<ID> ids) {
        return delete(idInfo.idsWhere(ids));
    }

    @Override
    public <ID extends Serializable> long updateById(T entityUpdate, Consumer<SQLUpdateClause> setter, @NotNull ID id) {
        return update(entityUpdate, setter, idInfo.idWhere(id));
    }
}
