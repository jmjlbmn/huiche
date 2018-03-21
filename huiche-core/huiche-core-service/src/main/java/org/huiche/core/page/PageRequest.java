package org.huiche.core.page;

import java.io.Serializable;

/**
 * 基本分页请求,对Spring原生PageRequest封装<br>
 * 兼容EasyUI
 *
 * @author Maning
 * @version 0.1
 */
public class PageRequest implements Serializable {
    private static final int DEFAULT_ROWS = 10;
    private static final int MAX_ROWS = 100;
    private Integer page = 1;
    private Integer rows = DEFAULT_ROWS;
    /**
     * 多列排序时,以逗号分隔
     */
    private String sort;
    private String order;

    public int getOffset() {
        return (page - 1) * rows;
    }

    public PageRequest() {
    }

    public PageRequest(Integer page, Integer rows) {
        this.page = page;
        this.rows = rows;
    }

    public PageRequest(Integer page, Integer rows, String sort, String order) {
        this.page = page;
        this.rows = rows;
        this.sort = sort;
        this.order = order;
    }

    public static PageRequest dft() {
        return new PageRequest();
    }

    public static PageRequest of(int page, int rows) {
        return new PageRequest(page, rows);
    }

    public Integer getPage() {
        return page;
    }

    public PageRequest setPage(Integer page) {
        this.page = page;
        return this;
    }

    public Integer getRows() {
        return rows;
    }

    public PageRequest setRows(Integer rows) {
        this.rows = null == rows ? DEFAULT_ROWS : rows > MAX_ROWS ? MAX_ROWS : rows;
        return this;
    }

    public String getSort() {
        return sort;
    }

    public PageRequest setSort(String sort) {
        this.sort = sort;
        return this;
    }

    public String getOrder() {
        return order;
    }

    public PageRequest setOrder(String order) {
        this.order = order;
        return this;
    }
}
