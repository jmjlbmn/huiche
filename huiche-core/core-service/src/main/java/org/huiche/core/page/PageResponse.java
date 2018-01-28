package org.huiche.core.page;

import java.util.ArrayList;
import java.util.List;

/**
 * 页面响应
 *
 * @param <T> 类型
 * @author Maning
 */
public class PageResponse<T> {
    /**
     * 总条数
     */
    private Long total = 0L;
    /**
     * 数据List
     */
    private List<T> rows = new ArrayList<>();

    public PageResponse<T> setTotal(Long total) {
        this.total = total;
        return this;
    }

    public PageResponse<T> setRows(List<T> rows) {
        this.rows = rows;
        return this;
    }

    public Long getTotal() {
        return total;
    }

    public List<T> getRows() {
        return rows;
    }

    public PageResponse() {
    }
}
