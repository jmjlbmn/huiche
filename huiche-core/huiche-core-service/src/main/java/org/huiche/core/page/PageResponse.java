package org.huiche.core.page;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * 页面响应
 *
 * @param <T> 类型
 * @author Maning
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString
public class PageResponse<T> {
    /**
     * 总条数
     */
    private Long total = 0L;
    /**
     * 数据List
     */
    private List<T> rows = new ArrayList<>();

    public PageResponse() {
    }
}
