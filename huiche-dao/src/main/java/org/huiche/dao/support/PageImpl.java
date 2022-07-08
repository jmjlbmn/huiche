package org.huiche.dao.support;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import org.huiche.domain.Page;
import org.huiche.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Maning
 */
public class PageImpl<T> implements Page<T> {

    private Long page;
    private Long size;
    private Long totalItem;
    private List<T> rows;

    public static <T> Page<T> of(Long page, Long size, Long totalItem, List<T> rows) {
        PageImpl<T> impl = new PageImpl<>();
        impl.page = page == null ? 1 : page;
        impl.size = size == null ? 10 : size;
        impl.totalItem = totalItem == null ? 0 : totalItem;
        impl.rows = rows;
        return impl;
    }

    public static <T> Page<T> of(Pageable pageable, QueryResults<T> result) {
        PageImpl<T> impl = new PageImpl<>();
        impl.page = pageable.page();
        impl.size = pageable.size();
        impl.totalItem = result.getTotal();
        impl.rows = result.getResults();
        return impl;
    }

    public static <T> Page<T> of(Pageable pageable, QueryResults<Tuple> result, Function<Tuple, T> mapper) {
        PageImpl<T> impl = new PageImpl<>();
        impl.page = pageable.page();
        impl.size = pageable.size();
        impl.totalItem = result.getTotal();
        impl.rows = result.getResults().stream().map(mapper).collect(Collectors.toList());
        return impl;
    }

    @Override
    public long getPage() {
        return page;
    }

    @Override
    public long getSize() {
        return size;
    }


    @Override
    public long getTotalItem() {
        return totalItem;
    }

    @Override
    public List<T> getRows() {
        return rows;
    }
}