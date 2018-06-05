package org.huiche.dao.provider;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.sql.RelationalPath;

import javax.annotation.Nonnull;

/**
 * 用于所有CRUD操作的基础属性提供接口
 *
 * @author Maning
 */
public interface PathProvider<T> {
    /**
     * 默认多排序
     *
     * @return 排序
     */
    @Nonnull
    default OrderSpecifier[] defaultMultiOrder() {
        return new OrderSpecifier[]{defaultOrder()};
    }

    /**
     * 表
     *
     * @return 表
     */
    @Nonnull
    RelationalPath<T> root();

    /**
     * 主键
     *
     * @return 主键
     */
    @Nonnull
    NumberPath<Long> pk();

    /**
     * 默认排序
     *
     * @return 默认排序
     */
    @Nonnull
    default OrderSpecifier defaultOrder() {
        return pk().desc();
    }
}
