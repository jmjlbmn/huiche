package org.huiche.dao.operation;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import org.huiche.dao.support.Query;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.Serializable;

/**
 * @author Maning
 */
public interface GetOperation<T> {
    /**
     * 通过ID获取实体
     *
     * @param id         ID
     * @param <ID>       ID类型
     * @param conditions 附加条件
     * @return 实体
     */
    @Nullable
    <ID extends Serializable> T getById(@NonNull ID id, @NonNull Predicate... conditions);

    /**
     * 通过ID获取某字段
     *
     * @param column     字段
     * @param id         ID
     * @param <ID>       ID类型
     * @param <Col>      字段类型
     * @param conditions 附加条件
     * @return 字段
     */
    @Nullable
    <ID extends Serializable, Col> Col getColumnById(@NonNull Expression<Col> column, @NonNull ID id, @NonNull Predicate... conditions);


    /**
     * 获取实体某些字段
     *
     * @param columns    字段列表
     * @param id         ID
     * @param <ID>       ID类型
     * @param conditions 附加条件
     * @return 包含某些字段的实体
     */
    @Nullable
    <ID extends Serializable> T getColumnsById(@NonNull Expression<?>[] columns, @NonNull ID id, @NonNull Predicate... conditions);

    /**
     * 通过ID获取DTO
     *
     * @param dtoClass   DTO类型
     * @param columns    字段
     * @param id         ID
     * @param <ID>       ID类型
     * @param <DTO>      DTO类型
     * @param conditions 附加条件
     * @return DTO
     */
    @Nullable
    <ID extends Serializable, DTO> DTO getDtoById(@NonNull Class<DTO> dtoClass, @NonNull Expression<?>[] columns, @NonNull ID id, @NonNull Predicate... conditions);

    /**
     * 获取唯一实体,若存在多个则报错
     *
     * @param conditions 条件
     * @return 实体
     */
    @Nullable
    T getOne(Predicate... conditions);

    /**
     * 获取唯一实体的字段,若存在多个则报错
     *
     * @param column     字段
     * @param conditions 条件
     * @param <Col>      字段类型
     * @return 多个字段的实体
     */
    @Nullable
    <Col> Col getColumnOne(@NonNull Expression<Col> column, @NonNull Predicate... conditions);

    /**
     * 获取唯一实体多个字段,若存在多个则报错
     *
     * @param columns    字段
     * @param conditions 条件
     * @return 实体
     */
    @Nullable
    T getColumnsOne(@NonNull Expression<?>[] columns, @NonNull Predicate... conditions);

    /**
     * 获取唯一DTO,若存在多个则报错
     *
     * @param dtoClass   DTO
     * @param columns    字段列表
     * @param conditions 条件
     * @param <DTO>      DTO类型
     * @return DTO
     */
    @Nullable
    <DTO> DTO getDtoOne(@NonNull Class<DTO> dtoClass, @NonNull Expression<?>[] columns, @NonNull Predicate... conditions);

    /**
     * 根据条件获取第一个实体
     *
     * @param conditions 条件
     * @return 实体
     */
    @Nullable
    default T getFirst(Predicate... conditions) {
        return getFirst(Query.of(conditions));
    }

    /**
     * 根据条件获取第一个实体
     *
     * @param query 查询参数
     * @return 实体
     */
    @Nullable
    T getFirst(Query query);

    /**
     * 根据条件获取第一个实体的字段
     *
     * @param column     字段
     * @param conditions 条件
     * @param <Col>      字段类型
     * @return 字段
     */
    @Nullable
    default <Col> Col getColumnFirst(Expression<Col> column, Predicate... conditions) {
        return getColumnFirst(column, Query.of(conditions));
    }

    /**
     * 根据条件获取第一个实体的字段
     *
     * @param column 字段
     * @param query  查询参数
     * @param <Col>  字段类型
     * @return 字段
     */
    @Nullable
    <Col> Col getColumnFirst(Expression<Col> column, Query query);

    /**
     * 根据条件获取第一个实体的多个字段
     *
     * @param columns    字段列表
     * @param conditions 条件
     * @return 含多个字段值的实体
     */
    @Nullable
    default T getColumnsFirst(Expression<?>[] columns, Predicate... conditions) {
        return getColumnsFirst(columns, Query.of(conditions));
    }

    /**
     * 根据条件获取第一个实体的多个字段
     *
     * @param columns 字段列表
     * @param query   查询参数
     * @return 含多个字段值的实体
     */
    @Nullable
    T getColumnsFirst(Expression<?>[] columns, Query query);

    /**
     * 根据条件获取第一个DTO
     *
     * @param dtoClass   DTO类型
     * @param columns    字段列表
     * @param conditions 条件
     * @param <DTO>      DTO类型
     * @return DTO
     */
    @Nullable
    default <DTO> DTO getDtoFirst(Class<DTO> dtoClass, Expression<?>[] columns, Predicate... conditions) {
        return getDtoFirst(dtoClass, columns, Query.of(conditions));
    }

    /**
     * 根据条件获取第一个DTO
     *
     * @param dtoClass DTO类型
     * @param columns  字段列表
     * @param query    查询参数
     * @param <DTO>    DTO类型
     * @return DTO
     */
    @Nullable
    <DTO> DTO getDtoFirst(Class<DTO> dtoClass, Expression<?>[] columns, Query query);
}
