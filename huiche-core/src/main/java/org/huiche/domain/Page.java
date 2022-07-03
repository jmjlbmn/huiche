package org.huiche.domain;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Maning
 */
public interface Page<T> {
    /**
     * 第几页,1base
     *
     * @return 页码
     */
    long getPage();

    /**
     * 总页数
     *
     * @return 总页数
     */
    default long getTotalPage() {
        boolean flag = getTotalItem() % getSize() == 0;
        long totalPage = getTotalItem() / getSize();
        return flag ? totalPage : totalPage + 1;
    }

    /**
     * 每页条数
     *
     * @return 每页条数
     */
    long getSize();

    /**
     * 总数
     *
     * @return 总数
     */
    long getTotalItem();

    /**
     * 数据列表
     *
     * @return 数据列表
     */
    @NotNull
    List<T> getRows();
}
