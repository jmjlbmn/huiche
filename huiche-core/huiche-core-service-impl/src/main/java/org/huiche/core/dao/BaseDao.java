package org.huiche.core.dao;

import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.dml.DefaultMapper;
import com.querydsl.sql.dml.SQLInsertClause;
import com.querydsl.sql.dml.SQLUpdateClause;
import org.hibernate.validator.HibernateValidator;
import org.huiche.core.entity.BaseEntity;
import org.huiche.core.exception.Assert;
import org.huiche.core.exception.DataBaseException;
import org.huiche.core.exception.SystemError;
import org.huiche.core.page.PageRequest;
import org.huiche.core.page.PageResponse;
import org.huiche.core.util.BaseUtil;
import org.huiche.core.util.DateUtil;
import org.huiche.core.util.QueryDslUtil;
import org.huiche.core.util.StringUtil;
import org.huiche.core.validation.ValidOnlyCreate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 基础数据库查询Dao
 *
 * @author Maning
 */
public abstract class BaseDao<T extends BaseEntity> {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected static final Validator VALIDATOR = Validation.byProvider(HibernateValidator.class)
            .configure()
            .failFast(true)
            .buildValidatorFactory().getValidator();
    @Resource
    protected SQLQueryFactory sqlQueryFactory;

    /**
     * 新增数据
     *
     * @param entity 实体
     * @return 变更条数
     */
    public long create(T entity) {
        Assert.notNull(SystemError.NOT_NULL, entity);
        entity = beforeCreate(entity);
        validOnCreate(entity);
        Assert.isNull("新增数据时ID不能有值", entity.getId());
        Long id = sqlQueryFactory.insert(root())
                .populate(entity)
                .executeWithKey(pk());
        Assert.ok("新增数据失败", BaseUtil.equals(1, id));
        entity.setId(id);
        return 1;
    }

    /**
     * 批量插入数据
     *
     * @param entityList 实体
     * @return ID
     */
    public long create(Collection<T> entityList) {
        return create(entityList, false);
    }

    /**
     * 批量插入数据
     *
     * @param entityList 实体
     * @param fast       是否快速插入,快速插入仅插入需要设置的字段忽略null,但要注意快速插入时,须保证插入的要插入的字段一致,例如要插入name,sex,age字段,批量插入的所有实体都必须设置且只能设置这三个属性的值,不能有null(可以空字符串),不能设置其他属性
     * @return 变更条数
     */
    public long create(Collection<T> entityList, boolean fast) {
        Assert.ok(SystemError.NOT_NULL, null != entityList);
        SQLInsertClause insert = sqlQueryFactory.insert(root());
        entityList.forEach(t -> {
            beforeCreate(t);
            Assert.isNull("新增数据时ID不能有值", t.getId());
            if (fast) {
                insert.populate(t).addBatch();
            } else {
                insert.populate(t, DefaultMapper.WITH_NULL_BINDINGS).addBatch();
            }
        });
        if (!insert.isEmpty()) {
            LinkedList<Long> ids = new LinkedList<>(insert.executeWithKeys(pk()));
            if (entityList.size() == ids.size()) {
                entityList.forEach(t -> {
                    t.setId(ids.poll());
                });
            }
            return ids.size();
        } else {
            insert.clear();
            return 0;
        }
    }

    /**
     * 根据ID更新实体
     *
     * @param entity 实体
     * @return 变更条数
     */
    public long update(T entity) {
        Assert.notNull(SystemError.NOT_NULL, entity);
        Assert.notNull("更新数据时,实体必须设置ID", entity.getId());
        entity = beforeUpdate(entity);
        validRegular(entity);
        return sqlQueryFactory.update(root()).populate(entity).where(pk().eq(entity.getId())).execute();
    }

    /**
     * 根据ID更新实体列表,必须设置ID,不设置ID将跳过
     *
     * @param entityList 实体列表
     * @return 变动条数
     */
    public long update(Collection<T> entityList) {
        Assert.ok(SystemError.NOT_NULL, null != entityList);
        SQLUpdateClause update = sqlQueryFactory.update(root());
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
    public long update(T entity, Predicate... predicate) {
        Assert.notNull(entity, predicate);
        // 强制不更新ID
        entity.setId(null);
        validRegular(entity);
        return sqlQueryFactory.update(root()).populate(entity).where(predicate).execute();
    }

    /**
     * 删除
     *
     * @param id 主键
     * @return 变更条数
     */
    public long delete(Long id) {
        return sqlQueryFactory.delete(root()).where(pk().eq(id)).execute();
    }

    /**
     * 删除
     *
     * @param id 主键
     * @return 变更条数
     */
    public long delete(Long... id) {
        return sqlQueryFactory.delete(root()).where(pk().in(id)).execute();
    }

    /**
     * 删除
     *
     * @param ids 主键
     * @return 变更条数
     */
    public long delete(Collection<Long> ids) {
        return sqlQueryFactory.delete(root()).where(pk().in(ids)).execute();
    }

    /**
     * 删除
     *
     * @param ids 逗号分隔的id
     * @return 变更条数
     */
    public long delete(String ids) {
        return sqlQueryFactory.delete(root()).where(pk().in(StringUtil.split2ListLong(ids))).execute();
    }


    /**
     * 删除
     *
     * @param predicate 条件
     * @return 变更条数
     */
    public long delete(Predicate... predicate) {
        return sqlQueryFactory.delete(root()).where(predicate).execute();
    }

    /**
     * 是否存在
     *
     * @param id ID
     * @return 是否存在
     */
    public boolean exists(Long id) {
        return sqlQueryFactory.from(root()).where(pk().eq(id)).fetchCount() > 0;
    }

    /**
     * 是否存在
     *
     * @param predicate 条件
     * @return 是否存在
     */
    public boolean exists(Predicate... predicate) {
        return sqlQueryFactory.from(root()).where(predicate).fetchCount() > 0;
    }

    /**
     * 通过主键查找
     *
     * @param id 主键
     * @return 实体
     */
    public T get(Long id) {
        Assert.notNull(id);
        return QueryDslUtil.one(sqlQueryFactory.selectFrom(root()).where(pk().eq(id)));
    }


    /**
     * 获取单条数据
     *
     * @param predicate 条件
     * @return 数据
     */
    public T get(Predicate... predicate) {
        Assert.notNull("单记录查询,比如传入查询条件且保证该条件可以取得最多一条数据", (Object[]) predicate);
        return QueryDslUtil.one(sqlQueryFactory.selectFrom(root()).where(predicate));
    }

    /**
     * 获取单条数据,有排序
     *
     * @param order     排序
     * @param predicate 条件
     * @return 数据
     */
    public T get(OrderSpecifier<?> order, Predicate... predicate) {
        Assert.notNull("单记录查询,比如传入查询条件且保证该条件可以取得最多一条数据", (Object[]) predicate);
        SQLQuery<T> query = sqlQueryFactory.selectFrom(root()).where(predicate);
        if (null != order) {
            query = query.orderBy(order);
        }
        return QueryDslUtil.one(query);
    }

    /**
     * 获取单条数据,有排序
     *
     * @param order     排序
     * @param predicate 条件
     * @return 数据
     */
    public T get(Predicate predicate, OrderSpecifier<?>... order) {
        Assert.notNull("单记录查询,比如传入查询条件且保证该条件可以取得最多一条数据", predicate);
        SQLQuery<T> query = sqlQueryFactory.selectFrom(root()).where(predicate);
        if (null != order && order.length > 0) {
            query = query.orderBy(order);
        }
        return QueryDslUtil.one(query);
    }

    /**
     * 查询单条数据的某些字段
     *
     * @param predicate 条件
     * @param columns   字段
     * @return 数据
     */
    public T getColumns(Predicate predicate, Expression<?>... columns) {
        Assert.notNull("单记录查询,比如传入查询条件且保证该条件可以取得最多一条数据", predicate);
        Assert.ok("查询字段不可为空", null != columns && columns.length > 0);
        return QueryDslUtil.one(sqlQueryFactory.select(
                Projections.fields(root(), columns)
        ).from(root()).where(predicate));
    }

    /**
     * 查询单条数据的某些字段
     *
     * @param predicate 条件
     * @param order     字段
     * @param columns   获取的字段
     * @return 数据
     */
    public T getColumns(Predicate predicate, OrderSpecifier<?> order, Expression<?>... columns) {
        Assert.notNull("单记录查询,比如传入查询条件且保证该条件可以取得最多一条数据", predicate);
        Assert.ok("查询字段不可为空", null != columns && columns.length > 0);
        SQLQuery<T> query = sqlQueryFactory.select(
                Projections.fields(root(), columns)
        ).from(root()).where(predicate);
        if (null != order) {
            query = query.orderBy(order);
        }
        return QueryDslUtil.one(query);
    }

    /**
     * 获取单个字段
     *
     * @param id id
     * @return 字段数据
     */
    public <Col> Col getOneColumn(Expression<Col> column, Long id) {
        return getOneColumn(column, pk().eq(id));
    }

    /**
     * 获取单个字段
     *
     * @param predicate 条件
     * @return 字段数据
     */
    public <Col> Col getOneColumn(Expression<Col> column, Predicate... predicate) {
        Assert.notNull("单记录查询,比如传入查询条件且保证该条件可以取得最多一条数据", (Object[]) predicate);
        return QueryDslUtil.one(sqlQueryFactory.select(column).from(root()).where(predicate));
    }

    /**
     * 获取单个字段
     *
     * @param predicate 条件
     * @return 字段数据
     */
    public <Col> Col getOneColumn(Expression<Col> column, OrderSpecifier<?> order, Predicate... predicate) {
        Assert.notNull("单记录查询,比如传入查询条件且保证该条件可以取得最多一条数据", (Object[]) predicate);
        return QueryDslUtil.one(sqlQueryFactory.select(column).from(root()).where(predicate).orderBy(order));
    }

    /**
     * 获取单个字段
     *
     * @param predicate 条件
     * @return 字段数据
     */
    public <Col> Col getOneColumn(Expression<Col> column, OrderSpecifier<?>[] order, Predicate... predicate) {
        Assert.notNull("单记录查询,比如传入查询条件且保证该条件可以取得最多一条数据", (Object[]) predicate);
        SQLQuery<Col> query = sqlQueryFactory.select(column).from(root()).where(predicate);
        if (null != order && order.length > 0) {
            query = query.orderBy(order);
        }
        return QueryDslUtil.one(query);
    }

    /**
     * 根据ID列表获取数据
     *
     * @param ids 逗号分隔的ID列表
     * @return 数据
     */
    public List<T> list(String ids) {
        return QueryDslUtil.list(sqlQueryFactory.selectFrom(root()).where(pk().in(StringUtil.split2ListLong(ids))));
    }

    /**
     * 根据ID列表获取数据
     *
     * @param ids ID列表
     * @return 数据
     */
    public List<T> list(Collection<Long> ids) {
        return QueryDslUtil.list(sqlQueryFactory.selectFrom(root()).where(pk().in(ids)));
    }

    /**
     * 根据ID列表获取数据
     *
     * @param ids ID列表
     * @return 数据
     */
    public List<T> list(Long[] ids) {
        return QueryDslUtil.list(sqlQueryFactory.selectFrom(root()).where(pk().in(ids)));
    }

    /**
     * 获取列表数据
     *
     * @param predicate 条件
     * @return 数据列表
     */
    public List<T> list(Predicate... predicate) {
        return list(null, null, predicate);
    }

    /**
     * 获取列表数据
     *
     * @param order     排序
     * @param predicate 条件
     * @return 数据列表
     */
    public List<T> list(OrderSpecifier<?> order, Predicate... predicate) {
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
    public List<T> list(OrderSpecifier<?> order, Long limit, Predicate... predicate) {
        SQLQuery<T> query = sqlQueryFactory.selectFrom(root());
        if (null != predicate) {
            query = query.where(predicate);
        }
        if (null != order) {
            query = query.orderBy(order);
        } else {
            query = query.orderBy(defaultOrder());
        }
        if (null != limit) {
            query = query.limit(limit);
        }
        return QueryDslUtil.list(query);
    }

    /**
     * 获取某些字段的列表数据
     *
     * @param predicate 条件
     * @param order     排序
     * @param columns   获取的字段
     * @return 数据
     */
    public List<T> listColumns(Predicate predicate, OrderSpecifier<?> order, Expression<?>... columns) {
        return listColumns(predicate, order, null, columns);
    }

    /**
     * 获取某些字段的列表数据
     *
     * @param order   排序
     * @param columns 获取的字段
     * @return 数据
     */
    public List<T> listColumns(OrderSpecifier<?> order, Expression<?>... columns) {
        return listColumns(null, order, columns);
    }

    /**
     * 获取某些字段的列表数据
     *
     * @param predicate 条件
     * @param columns   获取的字段
     * @return 数据
     */
    public List<T> listColumns(Predicate predicate, Expression<?>... columns) {
        return listColumns(predicate, null, columns);
    }

    /**
     * 获取某些字段的列表数据
     *
     * @param columns 获取的字段
     * @return 数据
     */
    public List<T> listColumns(Expression<?>... columns) {
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
    public List<T> listColumns(Predicate predicate, OrderSpecifier<?> order, Long limit, Expression<?>... columns) {
        Assert.ok("查询字段不可为空", null != columns && columns.length > 0);
        SQLQuery<T> query = sqlQueryFactory.select(
                Projections.fields(root(), columns)
        ).from(root());
        if (null != predicate) {
            query = query.where(predicate);
        }
        if (null != order) {
            query = query.orderBy(order);
        } else {
            query = query.orderBy(defaultOrder());
        }
        if (null != limit) {
            query = query.limit(limit);
        }
        return QueryDslUtil.list(query);
    }

    /**
     * 获取某个字段的列表
     *
     * @param column    字段
     * @param predicate 条件
     * @param <Col>     字段
     * @return 字段的列表
     */
    public <Col> List<Col> listColumn(Expression<Col> column, Predicate... predicate) {
        return listColumn(column, null, null, predicate);
    }

    /**
     * 获取某个字段的列表
     *
     * @param column 字段
     * @param <Col>  字段
     * @return 字段的列表
     */
    public <Col> List<Col> listColumn(Expression<Col> column) {
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
    public <Col> List<Col> listColumn(Expression<Col> column, OrderSpecifier<?> order, Predicate... predicate) {
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
    public <Col> List<Col> listColumn(Expression<Col> column, OrderSpecifier<?> order, Long limit, Predicate... predicate) {
        SQLQuery<Col> query = sqlQueryFactory.select(column).from(root());
        if (null != predicate) {
            query = query.where(predicate);
        }
        if (null != order) {
            query = query.orderBy(order);
        } else {
            query = query.orderBy(defaultOrder());
        }
        if (null != limit) {
            query = query.limit(limit);
        }
        return QueryDslUtil.list(query);
    }

    /**
     * 获取分页数据,默认方法,表结构简单时可以调用,结构复杂时请务必选择pageColumns
     *
     * @param pageRequest 分页请求
     * @return 分页数据
     */
    public PageResponse<T> page(PageRequest pageRequest) {
        return page(pageRequest, (Predicate[]) null);
    }

    /**
     * 获取分页数据,默认方法,表结构简单时可以调用,结构复杂时请务必选择pageColumns
     *
     * @param pageRequest 分页请求
     * @param predicate   条件
     * @return 分页数据
     */
    public PageResponse<T> page(PageRequest pageRequest, Predicate... predicate) {
        SQLQuery<T> query = sqlQueryFactory.selectFrom(root());
        if (null != predicate && predicate.length > 0) {
            query = query.where(predicate);
        }
        OrderSpecifier[] order = QueryDslUtil.parsePageRequest(pageRequest);
        if (null != order && order.length > 0) {
            query = query.orderBy(order);
        } else {
            query = query.orderBy(defaultOrder());
        }
        return QueryDslUtil.page(pageRequest, query);
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
    public PageResponse<T> pageColumns(Predicate predicate, OrderSpecifier<?> order, PageRequest pageRequest, Expression<?>... columns) {
        Assert.ok("查询字段不可为空", null != columns && columns.length > 0);
        SQLQuery<T> query = sqlQueryFactory.select(Projections.fields(root(), columns)).from(root());
        if (null != predicate) {
            query = query.where(predicate);
        }
        if (null != order) {
            query = query.orderBy(order);
        } else {
            OrderSpecifier[] orders = QueryDslUtil.parsePageRequest(pageRequest);
            if (null != orders && orders.length > 0) {
                query = query.orderBy(orders);
            } else {
                query = query.orderBy(defaultOrder());
            }
        }
        return QueryDslUtil.page(pageRequest, query);
    }

    /**
     * 分页获取某些字段的数据
     *
     * @param predicate   条件
     * @param pageRequest 分页
     * @param columns     字段
     * @return 分页的数据
     */
    public PageResponse<T> pageColumns(Predicate predicate, PageRequest pageRequest, Expression<?>... columns) {
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
    public PageResponse<T> pageColumns(OrderSpecifier<?> order, PageRequest pageRequest, Expression<?>... columns) {
        return pageColumns(null, order, pageRequest, columns);
    }

    /**
     * 分页获取某些字段的数据
     *
     * @param pageRequest 分页
     * @param columns     字段
     * @return 分页的数据
     */
    public PageResponse<T> pageColumns(PageRequest pageRequest, Expression<?>... columns) {
        return pageColumns(null, null, pageRequest, columns);
    }

    /**
     * 创建之前方法,在validOnCreate之前执行<br>
     * 主要用于需要初始化默认值的情况,如发布时间,状态,类型,关注数,访问数等等<br>
     * 默认进行创建时间和修改时间的处理
     *
     * @param entity 实体
     * @return 变更后的实体
     */
    protected T beforeCreate(T entity) {
        String time = DateUtil.nowTime();
        entity.setCreateTime(time).setModifyTime(time);
        return entity;
    }

    /**
     * 更新之前方法,在validOnUpdate之前执行<br>
     * 这个一般很少用,比如用户类,更新之前,需要加密密码
     *
     * @param entity 实体
     * @return 变更后的实体
     */
    protected T beforeUpdate(T entity) {
        entity.setCreateTime(null);
        entity.setModifyTime(DateUtil.nowTime());
        return entity;
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

    protected NumberPath<Long> pk = Expressions.numberPath(Long.class, PathMetadataFactory.forProperty(root(), "id"));

    protected void validOnCreate(T entity) {
        valid(entity, ValidOnlyCreate.class, Default.class);
    }

    protected void validRegular(T entity) {
        valid(entity, Default.class);
    }

    private void valid(T entity, Class<?>... groups) {
        if (doValid()) {
            Assert.notNull(entity);
            Set<ConstraintViolation<T>> errors = VALIDATOR.validate(entity, groups);
            if (!errors.isEmpty()) {
                throw new DataBaseException(errors.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList()));
            }
        }
    }

    protected boolean doValid() {
        return true;
    }

    /**
     * 默认排序
     *
     * @return 主键倒序
     */
    protected OrderSpecifier defaultOrder() {
        return pk().desc();
    }

}
