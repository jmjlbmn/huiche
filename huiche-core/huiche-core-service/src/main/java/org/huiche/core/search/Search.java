package org.huiche.core.search;


import com.querydsl.core.types.Predicate;

/**
 * @author Maning
 */
public interface Search {
    /**
     * 解析检索条件
     *
     * @return 检索条件
     */
    default Predicate get() {
        return SearchUtil.of(this);
    }
}
