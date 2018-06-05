package org.huiche.data.page;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 页面响应,提供总的记录条数和当前页数据List
 *
 * @param <T> 类型
 * @author Maning
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString
public class PageResponse<T> implements Serializable {
    /**
     * 总条数
     */
    private Long total = 0L;
    /**
     * 数据List
     */
    private List<T> rows = new ArrayList<>();
}
