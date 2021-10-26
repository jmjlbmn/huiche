package org.huiche.data.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 页面响应,提供总的记录条数和当前页数据List
 *
 * @param <T> 类型
 * @author Maning
 */
public class PageResponse<T> implements Serializable {
    /**
     * 总条数
     */
    private Long total = 0L;
    /**
     * 数据List
     */
    private List<T> rows = new ArrayList<>();

    public Long getTotal() {
        return total;
    }

    public PageResponse<T> setTotal(Long total) {
        this.total = total;
        return this;
    }

    public List<T> getRows() {
        return rows;
    }

    public PageResponse<T> setRows(List<T> rows) {
        this.rows = rows;
        return this;
    }
}
