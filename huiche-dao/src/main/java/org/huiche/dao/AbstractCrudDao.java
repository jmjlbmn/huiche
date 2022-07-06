package org.huiche.dao;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.dml.SQLDeleteClause;
import com.querydsl.sql.dml.SQLInsertClause;
import com.querydsl.sql.dml.SQLUpdateClause;
import org.huiche.dao.operation.CrudOperation;
import org.huiche.dao.support.PageImpl;
import org.huiche.dao.support.Q;
import org.huiche.dao.support.Querys;
import org.huiche.domain.Page;
import org.huiche.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Maning
 */
public abstract class AbstractCrudDao<T> extends Dao implements CrudOperation<T> {
    @Autowired
    protected RelationalPath<T> table;

    @Override
    public long count(Predicate... conditions) {
        return addWhere(sql.selectOne().from(table), conditions).fetchCount();
    }

    @Override
    public long count(T query) {
        return count(Querys.ofEntity(query, table));
    }

    protected <R> SQLQuery<R> addWhere(SQLQuery<R> query, Predicate... conditions) {
        if (conditions.length > 0) {
            query.where(conditions);
        }
        return query;
    }

    @Override
    public boolean exists(Predicate... conditions) {
        return Objects.equals(1, addWhere(sql.selectOne().from(table), conditions).fetchFirst());
    }

    @Override
    public T getOne(Predicate... conditions) {
        return addWhere(sql.selectFrom(table), conditions).fetchOne();
    }

    @Override
    public <Col> Col getColumnOne(Expression<Col> column, Predicate... conditions) {
        return addWhere(sql.select(column).from(table), conditions).fetchOne();
    }

    @Override
    public T getColumnsOne(Expression<?>[] columns, Predicate... conditions) {
        return addWhere(sql.select(Projections.fields(table, columns)).from(table), conditions).fetchOne();
    }

    @Override
    public List<T> list(T query) {
        return list(Querys.ofEntity(query, table));
    }

    @Override
    public Page<T> page(Pageable pageable, T query) {
        return page(Q.of(pageable).where(Querys.ofEntity(query, table)));
    }

    @Override
    public <DTO> DTO getDtoOne(Class<DTO> dtoClass, Expression<?>[] columns, Predicate... conditions) {
        return addWhere(sql.select(Projections.fields(dtoClass, columns)).from(table).where(), conditions).fetchOne();
    }

    @Override
    public T getFirst(Q q) {
        return q.use(sql.selectFrom(table)).fetchFirst();
    }

    @Override
    public <Col> Col getColumnFirst(Expression<Col> column, Q q) {
        return q.use(sql.select(column).from(table)).fetchFirst();
    }

    @Override
    public T getColumnsFirst(Expression<?>[] columns, Q q) {
        return q.use(sql.select(Projections.fields(table, columns)).from(table)).fetchFirst();
    }

    @Override
    public <DTO> DTO getDtoFirst(Class<DTO> dtoClass, Expression<?>[] columns, Q q) {
        return q.use(sql.select(Projections.fields(dtoClass, columns)).from(table)).fetchFirst();
    }

    @Override
    public List<T> list(Q q) {
        return q.use(sql.selectFrom(table)).fetch();
    }

    @Override
    public <Col> List<Col> listColumn(Path<Col> column, Q q) {
        return q.use(sql.select(column).from(table)).fetch();
    }

    @Override
    public List<T> listColumns(Expression<?>[] columns, Q q) {
        return q.use(sql.select(Projections.fields(table, columns)).from(table)).fetch();
    }

    @Override
    public <DTO> List<DTO> listDto(Class<DTO> dtoClass, Expression<?>[] columns, Q q) {
        return q.use(sql.select(Projections.fields(dtoClass, columns)).from(table)).fetch();
    }

    @Override
    public Page<T> page(Q q) {
        return PageImpl.of(q.useForPage(sql.selectFrom(table)).fetchResults());
    }

    @Override
    public Page<T> pageColumns(Expression<?>[] columns, Q q) {
        return pageImpl().apply(q.useForPage(sql.select(Projections.fields(table, columns)).from(table)).fetchResults());
    }

    @Override
    public <DTO> Page<DTO> pageDto(Class<DTO> dtoClass, Expression<?>[] columns, Q q) {
        return PageImpl.of(q.useForPage(sql.select(Projections.fields(dtoClass, columns)).from(table)).fetchResults());
    }

    @Override
    public <DTO> Page<DTO> pageDto(Function<Tuple, DTO> mapper, Expression<?>[] columns, Q q) {
        return PageImpl.of(q.useForPage(sql.select(columns).from(table)).fetchResults(), mapper);
    }

    @Override
    public long delete(Predicate... conditions) {
        SQLDeleteClause dml = sql.delete(table);
        if (conditions.length > 0) {
            dml.where(conditions);
        }
        return dml.execute();
    }

    @Override
    public long update(@Nullable T entityUpdate, @Nullable Consumer<SQLUpdateClause> setter, Predicate... conditions) {
        entityUpdate = beforeUpdate(entityUpdate);
        SQLUpdateClause dml = sql.update(table);
        if (entityUpdate != null) {
            dml.populate(dml);
        }
        if (conditions.length > 0) {
            dml.where(conditions);
        }
        if (setter != null) {
            setter.accept(dml);
        }
        return dml.execute();
    }

    protected long autoIncrementIdsExecuteAndSet(SQLInsertClause dml, IdInfo idInfo, List<T> entityList) {
        LinkedList<?> ids = new LinkedList<>(dml.executeWithKeys(idInfo.getPath()));
        int insertSize = ids.size();
        entityList.forEach(entity -> idInfo.setIdVal(entity, ids.poll()));
        return insertSize;
    }

    protected Function<QueryResults<T>, Page<T>> pageImpl() {
        return PageImpl::of;
    }
}
