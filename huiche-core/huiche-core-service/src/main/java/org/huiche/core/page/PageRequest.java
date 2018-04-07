package org.huiche.core.page;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * 基本分页请求,对Spring原生PageRequest封装
 *
 * @author Maning
 */
@Setter
@Getter
@Accessors(chain = true)
@ToString
public class PageRequest implements Serializable {
    /**
     * 默认获取10条
     */
    private static final int DEFAULT_ROWS = 10;
    /**
     * 最多获取1000条
     */
    private static final int MAX_ROWS = 1000;
    private Integer page = 1;
    private Integer rows = DEFAULT_ROWS;
    /**
     * 多列排序时,以逗号分隔
     */
    private String sort;
    private String order;

    public PageRequest() {
    }

    public PageRequest(int page, int rows) {
        this.page = page;
        this.rows = rows;
    }

    public PageRequest(int page, int rows, @Nonnull String sort, @Nonnull String order) {
        this.page = page;
        this.rows = rows;
        this.sort = sort;
        this.order = order;
    }

    @Nonnull
    public static PageRequest dft() {
        return new PageRequest();
    }

    @Nonnull
    public static PageRequest of(int page, int rows) {
        return new PageRequest(page, rows);
    }

    public long getOffset() {
        return (page - 1) * rows;
    }

    public PageRequest setRows(Integer rows) {
        this.rows = rows > MAX_ROWS ? MAX_ROWS : rows;
        return this;
    }
}
