package org.huiche.dao;

import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import org.huiche.core.exception.HuiCheException;
import org.huiche.core.util.DateUtil;
import org.huiche.core.util.StringUtil;
import org.huiche.dao.provider.BaseCrudDaoProvider;
import org.huiche.data.entity.BaseEntity;
import org.huiche.data.validation.ValidOnlyCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Qualifier("fastValidator")
    @Autowired(required = false)
    protected Validator validator;

    /**
     * 创建之前方法,在validOnCreate之前执行
     * 主要用于需要初始化默认值的情况,如发布时间,状态,类型,关注数,访问数等等
     * 默认进行创建时间和修改时间的处理
     *
     * @param entity 实体
     */
    @OverridingMethodsMustInvokeSuper
    @Override
    public void beforeCreate(@Nonnull T entity) {
        String time = DateUtil.nowTime();
        if (entity.getCreateTime() == null) {
            entity.setCreateTime(time);
        }
        if (entity.getModifyTime() == null) {
            entity.setModifyTime(time);
        }
    }

    /**
     * 更新之前方法,在validOnUpdate之前执行
     * 这个一般很少用,比如用户类,更新之前,需要加密密码
     *
     * @param entity 实体
     */
    @OverridingMethodsMustInvokeSuper
    @Override
    public void beforeUpdate(@Nonnull T entity) {
        entity.setCreateTime(null);
        entity.setModifyTime(DateUtil.nowTime());
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

    /**
     * 创建时验证,建议验证非空
     *
     * @param entity 实体对象
     */
    @Override
    public void validOnCreate(@Nonnull T entity) {
        valid(entity, ValidOnlyCreate.class, Default.class);
    }

    /**
     * 严重规则,建议验证长度
     *
     * @param entity 实体对象
     */
    @Override
    public void validRegular(@Nonnull T entity) {
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

    @Override
    public boolean createSetId() {
        return true;
    }
}
