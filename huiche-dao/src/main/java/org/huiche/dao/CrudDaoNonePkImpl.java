package org.huiche.dao;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.dml.DefaultMapper;
import com.querydsl.sql.dml.SQLInsertClause;
import com.querydsl.sql.dml.SQLUpdateClause;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Maning
 */
public class CrudDaoNonePkImpl<T> extends AbstractCrudDao<T> {
    private static final RuntimeException EXCEPTION = new RuntimeException("this operation need table has one and only primary key");

    public CrudDaoNonePkImpl(SQLQueryFactory sql, RelationalPath<T> table) {
        this.sql = sql;
        this.table = table;
    }

    @Override
    @NotNull
    public T create(@NotNull T entity) {
        beforeCreate(entity);
        sql.insert(table).populate(entity).execute();
        return entity;
    }

    @Override
    @NotNull
    public T createOrReplace(@NotNull T entity) {
        throw EXCEPTION;
    }

    @Override
    public long createBatch(@NotNull Collection<T> entityList) {
        SQLInsertClause dml = sql.insert(table);
        for (T entity : entityList) {
            beforeCreate(entity);
            dml.populate(entity, DefaultMapper.WITH_NULL_BINDINGS).addBatch();
        }
        return dml.execute();
    }

    @Override
    public <ID extends Serializable> boolean existsById(@NotNull ID id) {
        throw EXCEPTION;
    }

    @Override
    @Nullable
    public <ID extends Serializable> T getById(@NotNull ID id) {
        throw EXCEPTION;
    }

    @Override
    @Nullable
    public <ID extends Serializable, Col> Col getColumnById(@NotNull Expression<Col> column, @NotNull ID id) {
        throw EXCEPTION;
    }

    @Override
    @Nullable
    public <ID extends Serializable> T getColumnsById(Expression<?> @NotNull [] columns, @NotNull ID id) {
        throw EXCEPTION;
    }

    @Override
    @Nullable
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
    public <ID extends Serializable> List<T> listColumnsByIds(Expression<?> @NotNull [] columns, @NotNull Collection<ID> ids) {
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
