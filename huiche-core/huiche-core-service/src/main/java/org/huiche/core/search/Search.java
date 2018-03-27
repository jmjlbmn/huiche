package org.huiche.core.search;


import com.querydsl.core.types.Predicate;

/**
 * 筛选接口
 *
 * @author Maning
 */
public interface Search {
    /**
     * 解析检索条件
     * 复杂或不满足要求时建议重写
     *
     * @return 检索条件
     */
    default Predicate get() {
        return SearchUtil.of(this);
    }
}
