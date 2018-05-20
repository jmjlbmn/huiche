package org.huiche.dao;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.dml.DefaultMapper;
import com.querydsl.sql.dml.SQLInsertClause;
import com.querydsl.sql.dml.SQLUpdateClause;
import org.huiche.core.exception.HuiCheError;
import org.huiche.core.exception.HuiCheException;
import org.huiche.core.util.Assert;
import org.huiche.core.util.DateUtil;
import org.huiche.core.util.StringUtil;
import org.huiche.dao.util.QueryUtil;
import org.huiche.data.entity.BaseEntity;
import org.huiche.data.page.PageRequest;
import org.huiche.data.page.PageResponse;
import org.huiche.data.validation.ValidOnlyCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 基础数据库查询Dao
 *
 * @author Maning
 */
public abstract class BaseCrudDao<T extends BaseEntity<T>> extends BaseDao {
    /**
     * 主键
     */
    protected final NumberPath<Long> pk = Expressions.numberPath(Long.class, PathMetadataFactory.forProperty(root(), "id"));

    @Qualifier("fastValidator")
    @Autowired(required = false)
    protected Validator validator;

    /**
     * 新增数据并设置ID(默认,可通过复写doSetId改变)
     *
     * @param entity 实体
     * @return ID
     */
    public long create(@Nonnull T entity) {
        return create(entity, doSetId());
    }

    /**
     * 新增数据
     *
     * @param entity 实体
     * @param setId  是否设置ID
     * @return ID
     */
    public long create(@Nonnull T entity, boolean setId) {
        beforeCreate(entity);
        validOnCreate(entity);
        Long id = entity.getId();
        if (null == id) {
            id = sql().insert(root())
                    .populate(entity)
                    .executeWithKey(pk());
        } else {
            sql().insert(root())
                    .populate(entity)
                    .execute();
        }
        if (null == id) {
            throw new HuiCheException("新增数据失败");
        } else {
            if (setId) {
                entity.setId(id);
            }
            return id;
        }
    }

    /**
     * 批量插入数据(会忽略ID)
     *
     * @param entityList 实体
     * @return ID
     */
    public long create(@Nonnull Collection<T> entityList) {
        return create(entityList, false);
    }

    /**
     * 批量插入数据(会忽略ID)
     *
     * @param entityList 实体
     * @param fast       是否快速插入,快速插入仅插入需要设置的字段忽略null,但要注意快速插入时,须保证插入的要插入的字段一致,例如要插入name,sex,age字段,批量插入的所有实体都必须设置且只能设置这三个属性的值,不能有null(可以空字符串),不能设置其他属性
     * @return 变更条数
     */
    public long create(@Nonnull Collection<T> entityList, boolean fast) {
        SQLInsertClause insert = sql().insert(root());
        entityList.forEach(t -> {
            beforeCreate(t.setId(null));
            validOnCreate(t);
            if (fast) {
                insert.populate(t).addBatch();
            } else {
                insert.populate(t, DefaultMapper.WITH_NULL_BINDINGS).addBatch();
            }
        });
        long size = insert.getBatchCount();
        if (size > 0) {
            if (doSetId()) {
                LinkedList<Long> ids = new LinkedList<>(insert.executeWithKeys(pk()));
                if (entityList.size() == ids.size()) {
                    entityList.forEach(t -> t.setId(ids.poll()));
                }
            } else {
                insert.execute();
            }
        }
        insert.clear();
        return size;
    }

    /**
     * 批量插入数据,可以携带ID,但没有ID的不会进行ID赋值
     *
     * @param entityList 实体
     * @return 变更条数
     */
    public long createWithId(@Nonnull Collection<T> entityList) {
        SQLInsertClause insert = sql().insert(root());
        entityList.forEach(t -> {
            beforeCreate(t);
            validOnCreate(t);
            insert.populate(t, DefaultMapper.WITH_NULL_BINDINGS).addBatch();
        });
        long size = insert.getBatchCount();
        if (size > 0) {
            insert.execute();
        }
        insert.clear();
        return size;
    }

    /**
     * 根据ID更新实体
     *
     * @param entity 实体
     * @return 变更条数
     */
    public long update(@Nonnull T entity) {
        return update(entity, true);
    }

    /**
     * 根据ID更新实体
     *
     * @param entity     实体
     * @param ignoreNull 是否忽略空属性,忽略时仅对有值的字段进行更新,否则全部进行更新
     * @return 变更条数
     */
    public long update(@Nonnull T entity, boolean ignoreNull) {
        Assert.notNull(HuiCheError.UPDATE_MUST_HAVE_ID, entity.getId());
        beforeUpdate(entity);
        validRegular(entity);
        SQLUpdateClause update = sql().update(root());
        if (ignoreNull) {
            return update.populate(entity).where(pk().eq(entity.getId())).execute();
        } else {
            // 忽略null时,需要进行创建验证,防止数据出现问题
            validOnCreate(entity);
            return update.populate(entity, DefaultMapper.WITH_NULL_BINDINGS).where(pk().eq(entity.getId())).execute();
        }
    }

    /**
     * 根据ID更新实体列表,必须设置ID,不设置ID将跳过
     *
     * @param entityList 实体列表
     * @return 变动条数
     */
    public long update(@Nonnull Collection<T> entityList) {
        SQLUpdateClause update = sql().update(root());
        for (T entity : entityList) {
            Long id = entity.getId();
            if (null != id) {
                beforeUpdate(entity);
                validRegular(entity);
                update.populate(entity.setId(null)).where(pk().eq(id)).addBatch();
            }
        }
        if (!update.isEmpty()) {
            return update.execute();
        } else {
            update.clear();
            return 0;
        }
    }

    /**
     * 根据条件更新实体
     *
     * @param entity    要更新的内容
     * @param predicate 条件
     * @return 变更条数
     */
    public long update(@Nonnull T entity, @Nullable Predicate... predicate) {
        Assert.ok("更新时条件不能为空", null != predicate && predicate.length > 0);
        // 强制不更新ID
        entity.setId(null);
        validRegular(entity);
        return sql().update(root()).populate(entity).where(predicate).execute();
    }

    /**
     * 删除
     *
     * @param id 主键
     * @return 变更条数
     */
    public long delete(@Nonnull Long id) {
        return sql().delete(root()).where(pk().eq(id)).execute();
    }

    /**
     * 删除
     *
     * @param id 主键
     * @return 变更条数
     */
    public long delete(@Nonnull Long... id) {
        return sql().delete(root()).where(pk().in(id)).execute();
    }

    /**
     * 删除
     *
     * @param ids 主键
     * @return 变更条数
     */
    public long delete(@Nonnull Collection<Long> ids) {
        return sql().delete(root()).where(pk().in(ids)).execute();
    }

    /**
     * 删除
     *
     * @param ids 逗号分隔的id
     * @return 变更条数
     */
    public long delete(@Nonnull String ids) {
        return sql().delete(root()).where(pk().in(StringUtil.split2ListLong(ids))).execute();
    }

    /**
     * 删除
     *
     * @param predicate 条件
     * @return 变更条数
     */
    public long delete(@Nullable Predicate... predicate) {
        Assert.ok("删除时条件不能为空", null != predicate && predicate.length > 0);
        return sql().delete(root()).where(predicate).execute();
    }

    /**
     * 是否存在
     *
     * @param id ID
     * @return 是否存在
     */
    public boolean exists(@Nonnull Long id) {
        return sql().from(root()).where(pk().eq(id)).fetchCount() > 0;
    }

    /**
     * 是否存在
     *
     * @param predicate 条件
     * @return 是否存在
     */
    public boolean exists(@Nullable Predicate... predicate) {
        SQLQuery<?> query = sql().from(root());
        if (null != predicate && predicate.length > 0) {
            query = query.where(predicate);
        }
        return QueryUtil.count(query) > 0;
    }

    /**
     * 是否存在
     *
     * @param id        主键ID
     * @param predicate 条件
     * @return 是否存在
     */
    public boolean exists(@Nonnull Long id, @Nullable Predicate... predicate) {
        SQLQuery<?> query = sql().from(root()).where(pk().eq(id));
        if (null != predicate && predicate.length > 0) {
            query = query.where(predicate);
        }
        return QueryUtil.count(query) > 0;
    }

    /**
     * 通过主键查找
     *
     * @param id 主键
     * @return 实体
     */
    @Nullable
    public T get(@Nonnull Long id) {
        return QueryUtil.one(sql().selectFrom(root()).where(pk().eq(id)));
    }

    /**
     * 获取单条数据
     *
     * @param predicate 条件
     * @return 数据
     */
    @Nullable
    public T get(@Nullable Predicate... predicate) {
        SQLQuery<T> query = sql().selectFrom(root());
        if (null != predicate && predicate.length > 0) {
            query = query.where(predicate);
        }
        return QueryUtil.one(query);
    }

    /**
     * 获取单条数据
     *
     * @param id        主键ID
     * @param predicate 条件
     * @return 数据
     */
    @Nullable
    public T get(@Nonnull Long id, @Nullable Predicate... predicate) {
        SQLQuery<T> query = sql().selectFrom(root()).where(pk().eq(id));
        if (null != predicate && predicate.length > 0) {
            query = query.where(predicate);
        }
        return QueryUtil.one(query);
    }

    /**
     * 获取单条数据,有排序
     *
     * @param order     排序
     * @param predicate 条件
     * @return 数据
     */
    @Nullable
    public T get(@Nullable OrderSpecifier<?> order, @Nullable Predicate... predicate) {
        SQLQuery<T> query = sql().selectFrom(root());
        if (null != predicate && predicate.length > 0) {
            query = query.where(predicate);
        }
        if (null != order) {
            query = query.orderBy(order);
        }
        return QueryUtil.one(query);
    }

    /**
     * 查询单条数据的某些字段
     *
     * @param id      主键ID
     * @param columns 字段
     * @return 数据
     */
    @Nullable
    public T getColumns(@Nonnull Long id, @Nonnull Expression<?>... columns) {
        Assert.ok("要获取字段不能为空", columns.length > 0);
        return QueryUtil.one(sql().select(
                Projections.fields(root(), columns)
        ).from(root()).where(pk().eq(id)));
    }

    /**
     * 查询单条数据的某些字段
     *
     * @param predicate 条件
     * @param columns   字段
     * @return 数据
     */
    @Nullable
    public T getColumns(@Nullable Predicate predicate, @Nonnull Expression<?>... columns) {
        Assert.ok("要获取字段不能为空", columns.length > 0);
        SQLQuery<T> query = sql().select(
                Projections.fields(root(), columns)
        ).from(root());
        if (null != predicate) {
            query = query.where(predicate);
        }
        return QueryUtil.one(query);
    }

    /**
     * 查询单条数据的某些字段
     *
     * @param id        主键ID
     * @param predicate 条件
     * @param columns   字段
     * @return 数据
     */
    @Nullable
    public T getColumns(@Nonnull Long id, @Nullable Predicate predicate, @Nonnull Expression<?>... columns) {
        Assert.ok("要获取字段不能为空", columns.length > 0);
        SQLQuery<T> query = sql()
                .select(Projections.fields(root(), columns))
                .from(root())
                .where(pk().eq(id));
        if (null != predicate) {
            query = query.where(predicate);
        }
        return QueryUtil.one(query);
    }

    /**
     * 查询单条数据的某些字段
     *
     * @param predicate 条件
     * @param order     字段
     * @param columns   获取的字段
     * @return 数据
     */
    @Nullable
    public T getColumns(@Nullable Predicate predicate, @Nullable OrderSpecifier<?> order, @Nonnull Expression<?>... columns) {
        Assert.ok("要获取字段不能为空", columns.length > 0);
        SQLQuery<T> query = sql().select(
                Projections.fields(root(), columns)
        ).from(root());
        if (null != predicate) {
            query = query.where(predicate);
        }
        if (null != order) {
            query = query.orderBy(order);
        }
        return QueryUtil.one(query);
    }

    /**
     * 获取单个字段
     *
     * @param column 列
     * @param <Col>  列类型
     * @param id     id
     * @return 字段数据
     */
    @Nullable
    public <Col> Col getOneColumn(@Nonnull Expression<Col> column, @Nonnull Long id) {
        return getOneColumn(column, pk().eq(id));
    }

    /**
     * 获取单个字段
     *
     * @param column    列
     * @param <Col>     列类型
     * @param predicate 条件
     * @return 字段数据
     */
    @Nullable
    public <Col> Col getOneColumn(@Nonnull Expression<Col> column, @Nullable Predicate... predicate) {
        SQLQuery<Col> query = sql().select(column).from(root());
        if (null != predicate && predicate.length > 0) {
            query = query.where(predicate);
        }
        return QueryUtil.one(query);
    }

    /**
     * 获取单个字段
     *
     * @param column    列
     * @param <Col>     列类型
     * @param id        主键ID
     * @param predicate 条件
     * @return 字段数据
     */
    @Nullable
    public <Col> Col getOneColumn(@Nonnull Expression<Col> column, @Nonnull Long id, @Nullable Predicate... predicate) {
        SQLQuery<Col> query = sql().select(column).from(root()).where(pk().eq(id));
        if (null != predicate && predicate.length > 0) {
            query = query.where(predicate);
        }
        return QueryUtil.one(query);
    }

    /**
     * 获取单个字段
     *
     * @param column    列
     * @param <Col>     列类型
     * @param order     排序
     * @param predicate 条件
     * @return 字段数据
     */
    @Nullable
    public <Col> Col getOneColumn(@Nonnull Expression<Col> column, @Nonnull OrderSpecifier<?> order, @Nullable Predicate... predicate) {
        SQLQuery<Col> query = sql().select(column).from(root());
        if (null != predicate && predicate.length > 0) {
            query = query.where(predicate);
        }
        return QueryUtil.one(query.orderBy(order));
    }

    /**
     * 查询数量
     *
     * @param predicate 条件
     * @return 数量
     */
    public long count(@Nullable Predicate... predicate) {
        SQLQuery<T> query = sql().selectFrom(root());
        if (null != predicate && predicate.length > 0) {
            query = query.where(predicate);
        }
        return QueryUtil.count(query);
    }

    /**
     * 列表获取数据
     *
     * @return 数据
     */
    @Nonnull
    public List<T> list() {
        return QueryUtil.list(sql().selectFrom(root()).orderBy(defaultMultiOrder()));
    }

    /**
     * 根据ID列表获取数据
     *
     * @param ids 逗号分隔的ID列表
     * @return 数据
     */
    @Nonnull
    public List<T> list(@Nonnull String ids) {
        return QueryUtil.list(sql().selectFrom(root()).where(pk().in(StringUtil.split2ListLong(ids))).orderBy(defaultMultiOrder()));
    }

    /**
     * 根据ID列表获取数据
     *
     * @param ids ID列表
     * @return 数据
     */
    @Nonnull
    public List<T> list(@Nonnull Collection<Long> ids) {
        return QueryUtil.list(sql().selectFrom(root()).where(pk().in(ids)).orderBy(defaultMultiOrder()));
    }

    /**
     * 根据ID列表获取数据
     *
     * @param ids ID列表
     * @return 数据
     */
    @Nonnull
    public List<T> list(@Nonnull Long[] ids) {
        return QueryUtil.list(sql().selectFrom(root()).where(pk().in(ids)).orderBy(defaultMultiOrder()));
    }

    /**
     * 获取列表数据
     *
     * @param predicate 条件
     * @return 数据列表
     */
    @Nonnull
    public List<T> list(@Nullable Predicate... predicate) {
        return list(null, null, predicate);
    }

    /**
     * 获取列表数据
     *
     * @param order     排序
     * @param predicate 条件
     * @return 数据列表
     */
    @Nonnull
    public List<T> list(@Nullable OrderSpecifier<?> order, @Nullable Predicate... predicate) {
        return list(order, null, predicate);
    }

    /**
     * 获取列表数据
     *
     * @param order     排序
     * @param limit     获取条数
     * @param predicate 条件
     * @return 数据列表
     */
    @Nonnull
    public List<T> list(@Nullable OrderSpecifier<?> order, @Nullable Long limit, @Nullable Predicate... predicate) {
        SQLQuery<T> query = sql().selectFrom(root());
        if (null != predicate && predicate.length > 0) {
            query = query.where(predicate);
        }
        if (null != order) {
            query = query.orderBy(order);
        } else {
            query = query.orderBy(defaultMultiOrder());
        }
        if (null != limit) {
            query = query.limit(limit);
        }
        return QueryUtil.list(query);
    }

    /**
     * 获取某些字段的列表数据
     *
     * @param predicate 条件
     * @param order     排序
     * @param columns   获取的字段
     * @return 数据
     */
    @Nonnull
    public List<T> listColumns(@Nullable Predicate predicate, @Nullable OrderSpecifier<?> order, @Nonnull Expression<?>... columns) {
        return listColumns(predicate, order, null, columns);
    }

    /**
     * 获取某些字段的列表数据
     *
     * @param order   排序
     * @param columns 获取的字段
     * @return 数据
     */
    @Nonnull
    public List<T> listColumns(@Nullable OrderSpecifier<?> order, @Nonnull Expression<?>... columns) {
        return listColumns(null, order, columns);
    }

    /**
     * 获取某些字段的列表数据
     *
     * @param predicate 条件
     * @param columns   获取的字段
     * @return 数据
     */
    @Nonnull
    public List<T> listColumns(@Nullable Predicate predicate, @Nonnull Expression<?>... columns) {
        return listColumns(predicate, null, columns);
    }

    /**
     * 获取某些字段的列表数据
     *
     * @param columns 获取的字段
     * @return 数据
     */
    @Nonnull
    public List<T> listColumns(@Nonnull Expression<?>... columns) {
        return listColumns(null, null, columns);
    }

    /**
     * 获取某些字段的列表数据
     *
     * @param predicate 条件
     * @param order     排序
     * @param limit     条数
     * @param columns   获取的字段
     * @return 数据
     */
    @Nonnull
    public List<T> listColumns(@Nullable Predicate predicate, @Nullable OrderSpecifier<?> order, @Nullable Long limit, @Nonnull Expression<?>... columns) {
        Assert.ok("要获取字段不能为空", columns.length > 0);
        SQLQuery<T> query = sql().select(
                Projections.fields(root(), columns)
        ).from(root());
        if (null != predicate) {
            query = query.where(predicate);
        }
        if (null != order) {
            query = query.orderBy(order);
        } else {
            query = query.orderBy(defaultMultiOrder());
        }
        if (null != limit) {
            query = query.limit(limit);
        }
        return QueryUtil.list(query);
    }

    /**
     * 获取某个字段的列表
     *
     * @param column    字段
     * @param predicate 条件
     * @param <Col>     字段
     * @return 字段的列表
     */
    @Nonnull
    public <Col> List<Col> listColumn(@Nonnull Expression<Col> column, @Nullable Predicate... predicate) {
        return listColumn(column, null, null, predicate);
    }

    /**
     * 获取某个字段的列表
     *
     * @param column 字段
     * @param <Col>  字段
     * @return 字段的列表
     */
    @Nonnull
    public <Col> List<Col> listColumn(@Nonnull Expression<Col> column) {
        return listColumn(column, null, null, (Predicate[]) null);
    }

    /**
     * 获取某个字段的列表
     *
     * @param column    字段
     * @param order     排序
     * @param predicate 条件
     * @param <Col>     字段
     * @return 字段的列表
     */
    @Nonnull
    public <Col> List<Col> listColumn(@Nonnull Expression<Col> column, @Nullable OrderSpecifier<?> order, @Nullable Predicate... predicate) {
        return listColumn(column, order, null, predicate);
    }

    /**
     * 获取某个字段的列表
     *
     * @param column    字段
     * @param order     排序
     * @param limit     获取数量
     * @param predicate 条件
     * @param <Col>     字段
     * @return 字段的列表
     */
    @Nonnull
    public <Col> List<Col> listColumn(@Nonnull Expression<Col> column, @Nullable OrderSpecifier<?> order, @Nullable Long limit, @Nullable Predicate... predicate) {
        SQLQuery<Col> query = sql().select(column).from(root());
        if (null != predicate && predicate.length > 0) {
            query = query.where(predicate);
        }
        if (null != order) {
            query = query.orderBy(order);
        } else {
            query = query.orderBy(defaultMultiOrder());
        }
        if (null != limit) {
            query = query.limit(limit);
        }
        return QueryUtil.list(query);
    }

    /**
     * 获取分页数据,默认方法,表结构简单时可以调用,结构复杂时请务必选择pageColumns
     *
     * @param pageRequest 分页请求
     * @return 分页数据
     */
    @Nonnull
    public PageResponse<T> page(@Nullable PageRequest pageRequest) {
        return page(pageRequest, (Predicate[]) null);
    }

    /**
     * 获取分页数据,默认方法,表结构简单时可以调用,结构复杂时请务必选择pageColumns
     *
     * @param pageRequest 分页请求
     * @param predicate   条件
     * @return 分页数据
     */
    @Nonnull
    public PageResponse<T> page(@Nullable PageRequest pageRequest, @Nullable Predicate... predicate) {
        SQLQuery<T> query = sql().selectFrom(root());
        if (null != predicate && predicate.length > 0) {
            query = query.where(predicate);
        }
        OrderSpecifier[] order = QueryUtil.parsePageRequest(pageRequest);
        if (order.length > 0) {
            query = query.orderBy(order);
        } else {
            query = query.orderBy(defaultMultiOrder());
        }
        return QueryUtil.page(pageRequest, query);
    }

    /**
     * 分页获取某些字段的数据
     *
     * @param predicate   条件
     * @param order       排序
     * @param pageRequest 分页
     * @param columns     字段
     * @return 分页的数据
     */
    @Nonnull
    public PageResponse<T> pageColumns(@Nullable Predicate predicate, @Nullable OrderSpecifier<?> order, @Nullable PageRequest pageRequest, @Nonnull Expression<?>... columns) {
        Assert.ok("查询字段不可为空", columns.length > 0);
        SQLQuery<T> query = sql().select(Projections.fields(root(), columns)).from(root());
        if (null != predicate) {
            query = query.where(predicate);
        }
        if (null != order) {
            query = query.orderBy(order);
        } else {
            OrderSpecifier[] orders = QueryUtil.parsePageRequest(pageRequest);
            if (orders.length > 0) {
                query = query.orderBy(orders);
            } else {
                query = query.orderBy(defaultMultiOrder());
            }
        }
        return QueryUtil.page(pageRequest, query);
    }

    /**
     * 分页获取某些字段的数据
     *
     * @param predicate   条件
     * @param pageRequest 分页
     * @param columns     字段
     * @return 分页的数据
     */
    @Nonnull
    public PageResponse<T> pageColumns(@Nullable Predicate predicate, @Nullable PageRequest pageRequest, @Nonnull Expression<?>... columns) {
        return pageColumns(predicate, null, pageRequest, columns);
    }

    /**
     * 分页获取某些字段的数据
     *
     * @param order       排序
     * @param pageRequest 分页
     * @param columns     字段
     * @return 分页的数据
     */
    @Nonnull
    public PageResponse<T> pageColumns(@Nullable OrderSpecifier<?> order, @Nullable PageRequest pageRequest, @Nonnull Expression<?>... columns) {
        return pageColumns(null, order, pageRequest, columns);
    }

    /**
     * 分页获取某些字段的数据
     *
     * @param pageRequest 分页
     * @param columns     字段
     * @return 分页的数据
     */
    @Nonnull
    public PageResponse<T> pageColumns(@Nullable PageRequest pageRequest, @Nonnull Expression<?>... columns) {
        return pageColumns(null, null, pageRequest, columns);
    }

    /**
     * 创建之前方法,在validOnCreate之前执行
     * 主要用于需要初始化默认值的情况,如发布时间,状态,类型,关注数,访问数等等
     * 默认进行创建时间和修改时间的处理
     *
     * @param entity 实体
     */
    @OverridingMethodsMustInvokeSuper
    protected void beforeCreate(@Nonnull T entity) {
        String time = DateUtil.nowTime();
        entity.setCreateTime(time).setModifyTime(time);
    }

    /**
     * 更新之前方法,在validOnUpdate之前执行
     * 这个一般很少用,比如用户类,更新之前,需要加密密码
     *
     * @param entity 实体
     */
    @OverridingMethodsMustInvokeSuper
    protected void beforeUpdate(@Nonnull T entity) {
        entity.setCreateTime(null);
        entity.setModifyTime(DateUtil.nowTime());
    }

    /**
     * 表
     *
     * @return 表
     */
    protected abstract RelationalPath<T> root();

    /**
     * 主键
     *
     * @return 主键
     */
    protected NumberPath<Long> pk() {
        return pk;
    }

    /**
     * 创建时验证,建议验证非空
     *
     * @param entity 实体对象
     */
    protected void validOnCreate(@Nonnull T entity) {
        valid(entity, ValidOnlyCreate.class, Default.class);
    }

    /**
     * 严重规则,建议验证长度
     *
     * @param entity 实体对象
     */
    protected void validRegular(@Nonnull T entity) {
        valid(entity, Default.class);
    }

    /**
     * 进行验证
     *
     * @param entity 实体对象
     * @param groups 验证分组
     */
    private void valid(T entity, Class<?>... groups) {
        if (doValid() && null != validator) {
            Set<ConstraintViolation<T>> errors = validator.validate(entity, groups);
            if (!errors.isEmpty()) {
                throw new HuiCheException(StringUtil.join(errors.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList())));
            }
        }
    }

    /**
     * 是否进行验证,重写此方法不再进行验证
     *
     * @return 是否进行验证
     */
    protected boolean doValid() {
        return true;
    }

    /**
     * 是否在创建对象时SetId
     *
     * @return 是否进行验证
     */
    protected boolean doSetId() {
        return true;
    }

    /**
     * 默认多排序
     *
     * @return 主键倒序
     */
    @Nonnull
    protected OrderSpecifier[] defaultMultiOrder() {
        return new OrderSpecifier[]{defaultOrder()};
    }

    /**
     * 默认单排序
     *
     * @return 主键倒序
     */
    @Nonnull
    protected OrderSpecifier defaultOrder() {
        return pk().desc();
    }
}