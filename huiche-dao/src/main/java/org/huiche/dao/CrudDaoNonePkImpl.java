package org.huiche.dao;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.dml.DefaultMapper;
import com.querydsl.sql.dml.SQLInsertClause;
import com.querydsl.sql.dml.SQLUpdateClause;
import org.springframework.lang.Nullable;

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
    public <E extends T> E create(E entity) {
        beforeCreate(entity);
        sql.insert(table).populate(entity).execute();
        return entity;
    }

    @Override

    public <E extends T> E createOrReplace(E entity) {
        throw EXCEPTION;
    }

    @Override
    public <E extends T> long createBatch(Collection<E> entityList) {
        SQLInsertClause dml = sql.insert(table);
        for (T entity : entityList) {
            beforeCreate(entity);
            dml.populate(entity, DefaultMapper.WITH_NULL_BINDINGS).addBatch();
        }
        return dml.execute();
    }

    @Override
    public <ID extends Serializable> boolean existsById(ID id) {
        throw EXCEPTION;
    }

    @Override
    @Nullable
    public <ID extends Serializable> T getById(ID id) {
        throw EXCEPTION;
    }

    @Override
    @Nullable
    public <ID extends Serializable, Col> Col getColumnById(Expression<Col> column, ID id) {
        throw EXCEPTION;
    }

    @Override
    @Nullable
    public <ID extends Serializable> T getColumnsById(Expression<?>[] columns, ID id) {
        throw EXCEPTION;
    }

    @Override
    @Nullable
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
