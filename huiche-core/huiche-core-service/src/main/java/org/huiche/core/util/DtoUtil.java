package org.huiche.core.util;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.sql.RelationalPath;
import org.huiche.core.entity.BaseEntity;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Maning
 */
public class DtoUtil {
    /**
     * 扩展增加dto继承
     *
     * @param beanPath 实体类的查询类
     * @param columns  扩展的列
     * @param <T>      实体类
     * @return 所有查询的列
     */
    @Nonnull
    public static <T extends BaseEntity<T>> Expression[] extendColumn(@Nonnull RelationalPath<T> beanPath, @Nonnull Expression... columns) {
        List<Expression> list = new ArrayList<>();
        list.addAll(beanPath.getColumns());
        list.addAll(Arrays.asList(columns));
        return list.toArray(new Expression[0]);
    }

    /**
     * 获取继承bean的dto
     *
     * @param dtoClass dto的class
     * @param beanPath 实体类的查询类
     * @param columns  扩展的列
     * @param <T>      实体类
     * @param <DTO>    dto的类
     * @return dto
     */
    @Nonnull
    public static <DTO extends T, T extends BaseEntity<T>> QBean<DTO> extendBean(@Nonnull Class<DTO> dtoClass, @Nonnull RelationalPath<T> beanPath, @Nonnull Expression... columns) {
        return Projections.fields(dtoClass, extendColumn(beanPath, columns));
    }
}
