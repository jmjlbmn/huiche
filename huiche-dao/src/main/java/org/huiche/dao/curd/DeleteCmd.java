package org.huiche.dao.curd;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import org.huiche.core.exception.HuiCheException;
import org.huiche.core.util.Assert;
import org.huiche.core.util.StringUtil;
import org.huiche.dao.provider.PathProvider;
import org.huiche.dao.provider.SqlProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.SQLException;
import java.util.Collection;

/**
 * 删除操作
 * @author Maning
 */
public interface DeleteCmd<T> extends PathProvider<T>, SqlProvider {

    /**
     * 删除
     *
     * @param id 主键
     * @return 变更条数
     */
    default long delete(long id) {
        return delete(pk().eq(id));
    }

    /**
     * 删除
     *
     * @param id 主键
     * @return 变更条数
     */
    default long delete(@Nonnull Long... id) {
        return delete(pk().in(id));
    }

    /**
     * 删除
     *
     * @param ids 主键
     * @return 变更条数
     */
    default long delete(@Nonnull Collection<Long> ids) {
        return delete(pk().in(ids));
    }

    /**
     * 删除
     *
     * @param ids 逗号分隔的id
     * @return 变更条数
     */
    default long delete(@Nonnull String ids) {
        return delete(pk().in(StringUtil.split2ListLong(ids)));
    }

    /**
     * 删除
     *
     * @param id        ID
     * @param predicate 条件
     * @return 变更条数
     */
    default long delete(long id, @Nullable Predicate... predicate) {
        return delete(pk().eq(id), null == predicate ? null : ExpressionUtils.allOf(predicate));
    }

    /**
     * 删除
     *
     * @param predicate 条件
     * @return 变更条数
     */
    default long delete(@Nullable Predicate... predicate) {
        Assert.ok("删除时条件不能为空", null != predicate && predicate.length > 0);
        return sql().delete(root()).where(predicate).execute();
    }

    /**
     * 清空表
     */
    default void truncate() {
        String sql = "TRUNCATE TABLE " + root().getTableName();
        try {
            sql().getConnection().prepareStatement(sql).execute();
        } catch (SQLException e) {
            throw new HuiCheException("清空表出错", e);
        }
    }
}
