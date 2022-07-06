package org.huiche.dao.operation;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import org.huiche.dao.support.Q;

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

    <ID extends Serializable> List<T> listByIds(Collection<ID> ids);

    /**
     * 根据ID列表获取实体列表
     *
     * @param ids     ID列表
     * @param columns 字段列表
     * @param <ID>    ID类型
     * @return 实体列表
     */

    <ID extends Serializable> List<T> listColumnsByIds(Expression<?>[] columns, Collection<ID> ids);

    /**
     * 根据ID列表获取实体字段列表
     *
     * @param ids    ID列表
     * @param column 字段列
     * @param <ID>   ID类型
     * @return 实体列表
     */

    <ID extends Serializable, Col> List<Col> listColumnByIds(Path<Col> column, Collection<ID> ids);

    /**
     * 根据ID列表获取DTO列表
     *
     * @param dtoClass DTO类型
     * @param columns  字段列表
     * @param ids      ID列表
     * @param <ID>     ID类型
     * @return 实体列表
     */

    <ID extends Serializable, DTO> List<DTO> listDtoByIds(Class<DTO> dtoClass, Expression<?>[] columns, Collection<ID> ids);

    /**
     * 根据条件获取实体列表
     *
     * @param conditions 条件
     * @return 实体类别
     */

    default List<T> list(Predicate... conditions) {
        return list(Q.of(conditions));
    }


    /**
     * 根据实体获取实体列表
     */
    List<T> list(T query);

    /**
     * 根据查询参数获取实体列表
     *
     * @param q 查询参数
     * @return 实体列表
     */

    List<T> list(Q q);

    /**
     * 根据条件获取字段列表
     *
     * @param column     字段
     * @param conditions 条件
     * @return 实体类别
     */

    default <Col> List<Col> listColumn(Path<Col> column, Predicate... conditions) {
        return listColumn(column, Q.of(conditions));
    }

    /**
     * 根据条件获取字段列表
     *
     * @param column 字段
     * @param q      查询参数
     * @return 实体类别
     */

    <Col> List<Col> listColumn(Path<Col> column, Q q);

    /**
     * 根据查询参数获取实体多个属性列表
     *
     * @param columns    字段列表
     * @param conditions 条件
     * @return 实体多个属性列表
     */

    default List<T> listColumns(Expression<?>[] columns, Predicate... conditions) {
        return listColumns(columns, Q.of(conditions));
    }

    /**
     * 根据查询参数获取实体多个属性列表
     *
     * @param columns 字段列表
     * @param q       查询参数
     * @return 实体多个属性列表
     */

    List<T> listColumns(Expression<?>[] columns, Q q);

    /**
     * 根据查询参数获取DTO列表
     *
     * @param dtoClass   字段列表
     * @param columns    字段列表
     * @param conditions 条件
     * @return 实体多个属性列表
     */

    default <DTO> List<DTO> listDto(Class<DTO> dtoClass, Expression<?>[] columns, Predicate... conditions) {
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

    <DTO> List<DTO> listDto(Class<DTO> dtoClass, Expression<?>[] columns, Q q);
}
