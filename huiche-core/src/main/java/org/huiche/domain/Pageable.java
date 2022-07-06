package org.huiche.domain;

import java.io.Serializable;

/**
 * @author Maning
 */
public interface Pageable extends Serializable {
    /**
     * 获取页数
     *
     * @return 页数
     */
    long page();

    /**
     * 获取数量
     *
     * @return 数量
     */
    long size();
}
