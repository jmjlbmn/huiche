package org.huiche.data.page;

import org.huiche.core.util.HuiCheUtil;

import java.io.Serializable;

/**
 * 分页请求,提供第几页,每页多少记录,排序字段,顺序等参数
 *
 * @author Maning
 */
public class PageRequest implements Serializable {
    private int page = 1;
    private int rows = 10;
    /**
     * 多列排序时,以逗号分隔
     */
    private String sort;
    private String order;

    public PageRequest() {
    }

    public PageRequest(Integer page) {
        this.page = null == page ? 1 : page;
    }

    public PageRequest(Integer page, Integer rows) {
        this.page = null == page ? 1 : page;
        this.rows = null == rows ? 10 : rows;
    }

    public PageRequest(Integer page, Integer rows, String sort, String order) {
        this.page = null == page ? 1 : page;
        this.rows = null == rows ? 10 : rows;
        if (HuiCheUtil.isNotEmpty(sort)) {
            this.sort = sort;
        }
        if (HuiCheUtil.isNotEmpty(order)) {
            this.order = order;
        }
    }

    public long getOffset() {
        return (page - 1) * rows;
    }

    public PageRequest setPage(Integer page) {
        this.page = null == page ? 1 : page;
        return this;
    }

    public PageRequest setRows(Integer rows) {
        this.rows = null == rows ? 10 : rows;
        return this;
    }

    public PageRequest setSort(String sort) {
        if (HuiCheUtil.isNotEmpty(sort)) {
            this.sort = sort;
        }
        return this;
    }

    public PageRequest setOrder(String order) {
        if (HuiCheUtil.isNotEmpty(order)) {
            this.order = order;
        }
        return this;
    }

    public int getPage() {
        return page;
    }

    public int getRows() {
        return rows;
    }

    public String getSort() {
        return sort;
    }

    public String getOrder() {
        return order;
    }

    @Override
    public String toString() {
        return "PageRequest{" +
                "page=" + page +
                ", rows=" + rows +
                ", sort='" + sort + '\'' +
                ", order='" + order + '\'' +
                '}';
    }
}
