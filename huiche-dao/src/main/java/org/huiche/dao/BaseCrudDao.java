package org.huiche.dao;

import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import org.huiche.dao.provider.BaseCrudDaoProvider;
import org.huiche.data.entity.BaseEntity;

import javax.annotation.Nonnull;

/**
 * 通用的增删改查Dao
 *
 * @author Maning
 */
public abstract class BaseCrudDao<T extends BaseEntity<T>> extends BaseDao implements
        BaseCrudDaoProvider<T> {
    /**
     * 主键
     */
    protected final NumberPath<Long> pk = Expressions.numberPath(Long.class, PathMetadataFactory.forProperty(root(), "id"));

    /**
     * 创建之前方法,在validOnCreate之前执行
     * 主要用于需要初始化默认值的情况,如发布时间,状态,类型,关注数,访问数等等
     * 默认进行创建时间和修改时间的处理
     *
     * @param entity 实体
     */
    @Override
    public void beforeCreate(@Nonnull T entity) {
    }

    /**
     * 更新之前方法,在validOnUpdate之前执行
     * 这个一般很少用,比如用户类,更新之前,需要加密密码
     *
     * @param entity 实体
     */
    @Override
    public void beforeUpdate(@Nonnull T entity) {
    }

    /**
     * 主键
     *
     * @return 主键
     */
    @Override
    @Nonnull
    public NumberPath<Long> pk() {
        return pk;
    }

    @Override
    public boolean createSetId() {
        return true;
    }
}
