package org.huiche.dao;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.sql.dml.SQLUpdateClause;
import org.huiche.dao.operation.CrudOperation;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.lang.Nullable;

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
    public <E extends T> E create(E entity) {
        return delegate.create(entity);
    }

    @Override

    public <E extends T> E replace(E entity) {
        return delegate.replace(entity);
    }

    @Override
    public <E extends T> E save(E entity) {
        return delegate.save(entity);
    }

    @Override
    public <E extends T> long createBatch(Collection<E> entityList) {
        return delegate.createBatch(entityList);
    }

    @Override
    public <ID extends Serializable> long deleteById(ID id) {
        return delegate.deleteById(id);
    }

    @Override
    public <ID extends Serializable> long deleteByIds(Collection<ID> ids) {
        return delegate.deleteByIds(ids);
    }

    @Override
    public <ID extends Serializable> boolean existsById(ID id) {
        return delegate.existsById(id);
    }

    @Override
    @Nullable
    public <ID extends Serializable> T getById(ID id, Predicate... conditions) {
        return delegate.getById(id, conditions);
    }

    @Override
    @Nullable
    public <ID extends Serializable, Col> Col getColumnById(Expression<Col> column, ID id, Predicate... conditions) {
        return delegate.getColumnById(column, id, conditions);
    }

    @Override
    @Nullable
    public <ID extends Serializable> T getColumnsById(Expression<?>[] columns, ID id, Predicate... conditions) {
        return delegate.getColumnsById(columns, id, conditions);
    }

    @Override
    @Nullable
    public <ID extends Serializable, DTO> DTO getDtoById(Class<DTO> dtoClass, Expression<?>[] columns, ID id, Predicate... conditions) {
        return delegate.getDtoById(dtoClass, columns, id, conditions);
    }

    @Override

    public <ID extends Serializable> List<T> listByIds(Collection<ID> ids) {
        return delegate.listByIds(ids);
    }

    @Override

    public <ID extends Serializable> List<T> listColumnsByIds(Expression<?>[] columns, Collection<ID> ids) {
        return delegate.listColumnsByIds(columns, ids);
    }

    @Override

    public <ID extends Serializable, Col> List<Col> listColumnByIds(Path<Col> column, Collection<ID> ids) {
        return delegate.listColumnByIds(column, ids);
    }

    @Override

    public <ID extends Serializable, DTO> List<DTO> listDtoByIds(Class<DTO> dtoClass, Expression<?>[] columns, Collection<ID> ids) {
        return delegate.listDtoByIds(dtoClass, columns, ids);
    }

    @Override
    public <ID extends Serializable> long updateById(@Nullable T entityUpdate, @Nullable Consumer<SQLUpdateClause> setter, ID id) {
        return delegate.updateById(entityUpdate, setter, id);
    }

    @Override
    public void afterPropertiesSet() {
        delegate = CrudDaoFactory.create(this.sql, this.table);
    }
}
