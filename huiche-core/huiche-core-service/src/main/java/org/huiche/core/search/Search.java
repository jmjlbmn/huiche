package org.huiche.core.search;


import com.querydsl.core.types.Predicate;
import org.huiche.core.query.Query;

import javax.annotation.Nullable;

/**
 * 筛选接口
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
