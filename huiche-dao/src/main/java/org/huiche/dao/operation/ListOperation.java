package org.huiche.dao.operation;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import org.huiche.dao.Q;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author Maning
 */
public interface ListOperation<T> {
    /**
     * 根据ID列表获取实体列表
     *
     * @param ids  ID列表
     * @param <ID> ID类型
     * @return 实体列表
     */
    @NotNull <ID extends Serializable> List<T> listByIds(@NotNull Collection<ID> ids);

    /**
     * 根据ID列表获取实体列表
     *
     * @param ids     ID列表
     * @param columns 字段列表
     * @param <ID>    ID类型
     * @return 实体列表
     */
    @NotNull <ID extends Serializable> List<T> listColumnsByIds(@NotNull Expression<?>[] columns, @NotNull Collection<ID> ids);

    /**
     * 根据ID列表获取实体字段列表
     *
     * @param ids    ID列表
     * @param column 字段列
     * @param <ID>   ID类型
     * @return 实体列表
     */
    @NotNull <ID extends Serializable, Col> List<Col> listColumnByIds(@NotNull Path<Col> column, @NotNull Collection<ID> ids);

    /**
     * 根据ID列表获取DTO列表
     *
     * @param dtoClass DTO类型
     * @param columns  字段列表
     * @param ids      ID列表
     * @param <ID>     ID类型
     * @return 实体列表
     */
    @NotNull <ID extends Serializable, DTO> List<DTO> listDtoByIds(@NotNull Class<DTO> dtoClass, @NotNull Expression<?>[] columns, @NotNull Collection<ID> ids);

    /**
     * 根据条件获取实体列表
     *
     * @param conditions 条件
     * @return 实体类别
     */
    @NotNull
    default List<T> list(@NotNull Predicate... conditions) {
        return list(Q.of(conditions));
    }

    /**
     * 根据查询参数获取实体列表
     *
     * @param q 查询参数
     * @return 实体列表
     */
    @NotNull
    List<T> list(@NotNull Q q);

    /**
     * 根据条件获取字段列表
     *
     * @param column     字段
     * @param conditions 条件
     * @return 实体类别
     */
    @NotNull
    default <Col> List<Col> listColumn(@NotNull Path<Col> column, @NotNull Predicate... conditions) {
        return listColumn(column, Q.of(conditions));
    }

    /**
     * 根据条件获取字段列表
     *
     * @param column 字段
     * @param q      查询参数
     * @return 实体类别
     */
    @NotNull <Col> List<Col> listColumn(@NotNull Path<Col> column, @NotNull Q q);

    /**
     * 根据查询参数获取实体多个属性列表
     *
     * @param columns    字段列表
     * @param conditions 条件
     * @return 实体多个属性列表
     */
    @NotNull
    default List<T> listColumns(@NotNull Expression<?>[] columns, @NotNull Predicate... conditions) {
        return listColumns(columns, Q.of(conditions));
    }

    /**
     * 根据查询参数获取实体多个属性列表
     *
     * @param columns 字段列表
     * @param q       查询参数
     * @return 实体多个属性列表
     */
    @NotNull
    List<T> listColumns(@NotNull Expression<?>[] columns, @NotNull Q q);

    /**
     * 根据查询参数获取DTO列表
     *
     * @param dtoClass   字段列表
     * @param columns    字段列表
     * @param conditions 条件
     * @return 实体多个属性列表
     */
    @NotNull
    default <DTO> List<DTO> listDto(@NotNull Class<DTO> dtoClass, @NotNull Expression<?>[] columns, @NotNull Predicate... conditions) {
        return listDto(dtoClass, columns, Q.of(conditions));
    }

    /**
     * 根据查询参数获取DTO列表
     *
     * @param dtoClass 字段列表
     * @param columns  字段列表
     * @param q        查询参数
     * @return 实体多个属性列表
     */
    @NotNull <DTO> List<DTO> listDto(@NotNull Class<DTO> dtoClass, @NotNull Expression<?>[] columns, @NotNull Q q);
}
