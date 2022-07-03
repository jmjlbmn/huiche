package org.huiche.dao.operation;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import org.huiche.dao.Q;
import org.huiche.domain.Page;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * @author Maning
 */
public interface PageOperation<T> {
    /**
     * 分页获取实体
     *
     * @param q 查询参数
     * @return 分页结果
     */
    @NotNull
    Page<T> page(@NotNull Q q);

    /**
     * 分页查询实体的某些字段
     *
     * @param columns 字段列表
     * @param q       查询参数
     * @return 包含查询字段的实体
     */
    @NotNull
    Page<T> pageColumns(@NotNull Expression<?>[] columns, @NotNull Q q);

    /**
     * 分页查询DTO
     *
     * @param dtoClass DTO类型
     * @param columns  字段列表
     * @param q        查询参数
     * @param <DTO>    DTO类型
     * @return DTO分页
     */
    @NotNull <DTO> Page<DTO> pageDto(@NotNull Class<DTO> dtoClass, @NotNull Expression<?>[] columns, @NotNull Q q);

    /**
     * 分页查询DTO
     *
     * @param mapper  自定义转换
     * @param columns 字段列表
     * @param q       查询参数
     * @param <DTO>   DTO类型
     * @return DTO分页
     */
    @NotNull <DTO> Page<DTO> pageDto(@NotNull Function<Tuple, DTO> mapper, @NotNull Expression<?>[] columns, @NotNull Q q);
}
