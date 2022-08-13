package org.huiche.dao.operation;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import org.huiche.dao.support.Query;
import org.huiche.domain.Page;
import org.huiche.domain.Pageable;
import org.springframework.lang.Nullable;

import java.util.function.Function;

/**
 * @author Maning
 */
public interface PageOperation<T> {
    /**
     * 分页获取实体
     *
     * @param query 查询参数
     * @return 分页结果
     */

    Page<T> page(Query query);

    /**
     * 分页获取实体
     *
     * @param pageable 分页
     * @return 分页结果
     */

    default Page<T> page(Pageable pageable) {
        return page(Query.of(pageable.page(), pageable.size()));
    }

    /**
     * 分页获取实体,通过实体条件查询
     *
     * @param pageable 分页
     * @param query    查询
     * @return 分页结果
     */
    Page<T> page(Pageable pageable, T query);

    /**
     * 分页获取实体
     *
     * @param pageable 分页
     * @param order    排序
     * @return 分页结果
     */

    default Page<T> page(Pageable pageable, @Nullable OrderSpecifier<?> order) {
        return page(Query.of(pageable.page(), pageable.size()).order(order));
    }


    /**
     * 分页查询实体的某些字段
     *
     * @param columns 字段列表
     * @param query       查询参数
     * @return 包含查询字段的实体
     */

    Page<T> pageColumns(Expression<?>[] columns, Query query);

    /**
     * 分页查询DTO
     *
     * @param dtoClass DTO类型
     * @param columns  字段列表
     * @param query        查询参数
     * @param <DTO>    DTO类型
     * @return DTO分页
     */

    <DTO> Page<DTO> pageDto(Class<DTO> dtoClass, Expression<?>[] columns, Query query);

    /**
     * 分页查询DTO
     *
     * @param mapper  自定义转换
     * @param columns 字段列表
     * @param query       查询参数
     * @param <DTO>   DTO类型
     * @return DTO分页
     */

    <DTO> Page<DTO> pageDto(Function<Tuple, DTO> mapper, Expression<?>[] columns, Query query);
}
