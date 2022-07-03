package org.huiche.dao;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.sql.dml.SQLUpdateClause;
import org.huiche.dao.operation.CrudOperation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.InitializingBean;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Maning
 */
public class CrudDao<T> extends AbstractCrudDao<T> implements InitializingBean {
    private CrudOperation<T> delegate;

    @Override
    @NotNull
    public T create(@NotNull T entity) {
        return delegate.create(entity);
    }

    @Override
    @NotNull
    public T createOrReplace(@NotNull T entity) {
        return delegate.createOrReplace(entity);
    }

    @Override
    public long createBatch(@NotNull Collection<T> entityList) {
        return delegate.createBatch(entityList);
    }

    @Override
    public <ID extends Serializable> long deleteById(@NotNull ID id) {
        return delegate.deleteById(id);
    }

    @Override
    public <ID extends Serializable> long deleteByIds(@NotNull Collection<ID> ids) {
        return delegate.deleteByIds(ids);
    }

    @Override
    public <ID extends Serializable> boolean existsById(@NotNull ID id) {
        return delegate.existsById(id);
    }

    @Override
    @Nullable
    public <ID extends Serializable> T getById(@NotNull ID id) {
        return delegate.getById(id);
    }

    @Override
    @Nullable
    public <ID extends Serializable, Col> Col getColumnById(@NotNull Expression<Col> column, @NotNull ID id) {
        return delegate.getColumnById(column, id);
    }

    @Override
    @Nullable
    public <ID extends Serializable> T getColumnsById(Expression<?> @NotNull [] columns, @NotNull ID id) {
        return delegate.getColumnsById(columns, id);
    }

    @Override
    @Nullable
    public <ID extends Serializable, DTO> DTO getDtoById(@NotNull Class<DTO> dtoClass, Expression<?> @NotNull [] columns, @NotNull ID id) {
        return delegate.getDtoById(dtoClass, columns, id);
    }

    @Override
    @NotNull
    public <ID extends Serializable> List<T> listByIds(@NotNull Collection<ID> ids) {
        return delegate.listByIds(ids);
    }

    @Override
    @NotNull
    public <ID extends Serializable> List<T> listColumnsByIds(Expression<?> @NotNull [] columns, @NotNull Collection<ID> ids) {
        return delegate.listColumnsByIds(columns, ids);
    }

    @Override
    @NotNull
    public <ID extends Serializable, Col> List<Col> listColumnByIds(@NotNull Path<Col> column, @NotNull Collection<ID> ids) {
        return delegate.listColumnByIds(column, ids);
    }

    @Override
    @NotNull
    public <ID extends Serializable, DTO> List<DTO> listDtoByIds(@NotNull Class<DTO> dtoClass, Expression<?> @NotNull [] columns, @NotNull Collection<ID> ids) {
        return delegate.listDtoByIds(dtoClass, columns, ids);
    }

    @Override
    public <ID extends Serializable> long updateById(T entityUpdate, Consumer<SQLUpdateClause> setter, @NotNull ID id) {
        return delegate.updateById(entityUpdate, setter, id);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        delegate = CrudDaoFactory.create(this.sql, this.table);
    }
}
