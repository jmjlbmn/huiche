package org.huiche.data.page;

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
    private int page = 1;
    private int rows = 10;
    /**
     * 多列排序时,以逗号分隔
     */
    private String sort;
    private String order;

    public PageRequest() {
    }

    public PageRequest(int page) {
        this.page = page;
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

    public long getOffset() {
        return (page - 1) * rows;
    }
}
