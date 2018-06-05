package org.huiche.data.search;


import com.querydsl.core.types.Predicate;
import org.huiche.data.query.Query;

import javax.annotation.Nullable;

/**
 * 筛选接口,用于前端传递的自定义筛选
 *
 * @author Maning
 */
public interface Search extends Query {
    /**
     * 获取检索条件
     *
     * @return 获取检索条件
     */
    @Nullable
    Predicate get();
}
