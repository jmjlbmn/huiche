package org.huiche.dao.operation;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import org.huiche.dao.Q;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * @author Maning
 */
public interface GetOperation<T> {
    /**
     * 通过ID获取实体
     *
     * @param id   ID
     * @param <ID> ID类型
     * @return 实体
     */
    @Nullable <ID extends Serializable> T getById(@NotNull ID id);

    /**
     * 通过ID获取某字段
     *
     * @param column 字段
     * @param id     ID
     * @param <ID>   ID类型
     * @param <Col>  字段类型
     * @return 字段
     */
    @Nullable <ID extends Serializable, Col> Col getColumnById(@NotNull Expression<Col> column, @NotNull ID id);


    /**
     * 获取实体某些字段
     *
     * @param columns 字段列表
     * @param id      ID
     * @param <ID>    ID类型
     * @return 包含某些字段的实体
     */
    @Nullable <ID extends Serializable> T getColumnsById(@NotNull Expression<?>[] columns, @NotNull ID id);

    /**
     * 通过ID获取DTO
     *
     * @param dtoClass DTO类型
     * @param columns  字段
     * @param id       ID
     * @param <ID>     ID类型
     * @param <DTO>    DTO类型
     * @return DTO
     */
    @Nullable <ID extends Serializable, DTO> DTO getDtoById(@NotNull Class<DTO> dtoClass, @NotNull Expression<?>[] columns, @NotNull ID id);

    /**
     * 获取唯一实体,若存在多个则报错
     *
     * @param conditions 条件
     * @return 实体
     */
    @Nullable
    T getOne(@NotNull Predicate... conditions);

    /**
     * 获取唯一实体的字段,若存在多个则报错
     *
     * @param column     字段
     * @param conditions 条件
     * @param <Col>      字段类型
     * @return 多个字段的实体
     */
    @Nullable <Col> Col getColumnOne(@NotNull Expression<Col> column, @NotNull Predicate... conditions);

    /**
     * 获取唯一实体多个字段,若存在多个则报错
     *
     * @param columns    字段
     * @param conditions 条件
     * @return 实体
     */
    @Nullable
    T getColumnsOne(@NotNull Expression<?>[] columns, @NotNull Predicate... conditions);

    /**
     * 获取唯一DTO,若存在多个则报错
     *
     * @param dtoClass   DTO
     * @param columns    字段列表
     * @param conditions 条件
     * @param <DTO>      DTO类型
     * @return DTO
     */
    @Nullable <DTO> DTO getDtoOne(@NotNull Class<DTO> dtoClass, @NotNull Expression<?>[] columns, @NotNull Predicate... conditions);

    /**
     * 根据条件获取第一个实体
     *
     * @param conditions 条件
     * @return 实体
     */
    @Nullable
    default T getFirst(@NotNull Predicate... conditions) {
        return getFirst(Q.of(conditions));
    }

    /**
     * 根据条件获取第一个实体
     *
     * @param q 查询参数
     * @return 实体
     */
    @Nullable
    T getFirst(@NotNull Q q);

    /**
     * 根据条件获取第一个实体的字段
     *
     * @param column     字段
     * @param conditions 条件
     * @param <Col>      字段类型
     * @return 字段
     */
    @Nullable
    default <Col> Col getColumnFirst(@NotNull Expression<Col> column, @NotNull Predicate... conditions) {
        return getColumnFirst(column, Q.of(conditions));
    }

    /**
     * 根据条件获取第一个实体的字段
     *
     * @param column 字段
     * @param q      查询参数
     * @param <Col>  字段类型
     * @return 字段
     */
    @Nullable <Col> Col getColumnFirst(@NotNull Expression<Col> column, @NotNull Q q);

    /**
     * 根据条件获取第一个实体的多个字段
     *
     * @param columns    字段列表
     * @param conditions 条件
     * @return 含多个字段值的实体
     */
    @Nullable
    default T getColumnsFirst(@NotNull Expression<?>[] columns, @NotNull Predicate... conditions) {
        return getColumnsFirst(columns, Q.of(conditions));
    }

    /**
     * 根据条件获取第一个实体的多个字段
     *
     * @param columns 字段列表
     * @param q       查询参数
     * @return 含多个字段值的实体
     */
    @Nullable
    T getColumnsFirst(@NotNull Expression<?>[] columns, @NotNull Q q);

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
    default <DTO> DTO getDtoFirst(@NotNull Class<DTO> dtoClass, @NotNull Expression<?>[] columns, @NotNull Predicate... conditions) {
        return getDtoFirst(dtoClass, columns, Q.of(conditions));
    }

    /**
     * 根据条件获取第一个DTO
     *
     * @param dtoClass DTO类型
     * @param columns  字段列表
     * @param q        查询参数
     * @param <DTO>    DTO类型
     * @return DTO
     */
    @Nullable <DTO> DTO getDtoFirst(@NotNull Class<DTO> dtoClass, @NotNull Expression<?>[] columns, @NotNull Q q);
}
