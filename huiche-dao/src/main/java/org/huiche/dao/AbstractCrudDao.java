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
import org.huiche.domain.Page;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

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
    public long count(@NotNull Predicate... conditions) {
        return addWhere(sql.selectOne().from(table), conditions).fetchCount();
    }

    protected <R> SQLQuery<R> addWhere(SQLQuery<R> query, Predicate... conditions) {
        if (conditions != null && conditions.length > 0) {
            query.where(conditions);
        }
        return query;
    }

    @Override
    public boolean exists(@NotNull Predicate... conditions) {
        return Objects.equals(1, addWhere(sql.selectOne().from(table), conditions).fetchFirst());
    }

    @Override
    public T getOne(@NotNull Predicate... conditions) {
        return getOne(addWhere(sql.selectFrom(table), conditions));
    }

    public static <R> R getOne(@NotNull SQLQuery<R> query) {
        List<R> list = query.limit(2).fetch();
        int size = list.size();
        if (size > 1) {
            throw new IncorrectResultSizeDataAccessException(1, size);
        } else if (size == 1) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public <Col> Col getColumnOne(@NotNull Expression<Col> column, @NotNull Predicate... conditions) {
        return getOne(addWhere(sql.select(column).from(table), conditions));
    }

    @Override
    public T getColumnsOne(@NotNull Expression<?>[] columns, @NotNull Predicate... conditions) {
        return getOne(addWhere(sql.select(Projections.fields(table, columns)).from(table), conditions));
    }

    @Override
    public <DTO> DTO getDtoOne(@NotNull Class<DTO> dtoClass, @NotNull Expression<?>[] columns, @NotNull Predicate... conditions) {
        return getOne(addWhere(sql.select(Projections.fields(dtoClass, columns)).from(table).where(), conditions));
    }

    @Override
    public @Nullable T getFirst(@NotNull Q q) {
        return q.use(sql.selectFrom(table)).fetchFirst();
    }

    @Override
    public <Col> @Nullable Col getColumnFirst(@NotNull Expression<Col> column, @NotNull Q q) {
        return q.use(sql.select(column).from(table)).fetchFirst();
    }

    @Override
    public @Nullable T getColumnsFirst(@NotNull Expression<?>[] columns, @NotNull Q q) {
        return q.use(sql.select(Projections.fields(table, columns)).from(table)).fetchFirst();
    }

    @Override
    public <DTO> @Nullable DTO getDtoFirst(@NotNull Class<DTO> dtoClass, @NotNull Expression<?>[] columns, @NotNull Q q) {
        return q.use(sql.select(Projections.fields(dtoClass, columns)).from(table)).fetchFirst();
    }

    @Override
    public @NotNull List<T> list(@NotNull Q q) {
        return q.use(sql.selectFrom(table)).fetch();
    }

    @Override
    public @NotNull <Col> List<Col> listColumn(@NotNull Path<Col> column, @NotNull Q q) {
        return q.use(sql.select(column).from(table)).fetch();
    }

    @Override
    public @NotNull List<T> listColumns(@NotNull Expression<?>[] columns, @NotNull Q q) {
        return q.use(sql.select(Projections.fields(table, columns)).from(table)).fetch();
    }

    @Override
    public @NotNull <DTO> List<DTO> listDto(@NotNull Class<DTO> dtoClass, @NotNull Expression<?>[] columns, @NotNull Q q) {
        return q.use(sql.select(Projections.fields(dtoClass, columns)).from(table)).fetch();
    }

    @Override
    public @NotNull Page<T> page(@NotNull Q q) {
        return PageImpl.of(q.useForPage(sql.selectFrom(table)).fetchResults());
    }

    @Override
    public @NotNull Page<T> pageColumns(@NotNull Expression<?>[] columns, @NotNull Q q) {
        return pageImpl().apply(q.useForPage(sql.select(Projections.fields(table, columns)).from(table)).fetchResults());
    }

    @Override
    public @NotNull <DTO> Page<DTO> pageDto(@NotNull Class<DTO> dtoClass, @NotNull Expression<?>[] columns, @NotNull Q q) {
        return PageImpl.of(q.useForPage(sql.select(Projections.fields(dtoClass, columns)).from(table)).fetchResults());
    }

    @Override
    public @NotNull <DTO> Page<DTO> pageDto(@NotNull Function<Tuple, DTO> mapper, @NotNull Expression<?>[] columns, @NotNull Q q) {
        return PageImpl.of(q.useForPage(sql.select(columns).from(table)).fetchResults(), mapper);
    }

    @Override
    public long delete(@NotNull Predicate... conditions) {
        SQLDeleteClause dml = sql.delete(table);
        if (conditions.length > 0) {
            dml.where(conditions);
        }
        return dml.execute();
    }

    @Override
    public long update(@Nullable T entityUpdate, @Nullable Consumer<SQLUpdateClause> setter, @NotNull Predicate... conditions) {
        entityUpdate = beforeUpdate(entityUpdate);
        SQLUpdateClause dml = sql.update(table);
        if (entityUpdate != null) {
            dml.populate(dml);
        }
        if (conditions != null && conditions.length > 0) {
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
