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
    private Long total;
    private List<T> content;

    public static <T> Page<T> of(Long page, Long size, Long totalItem, List<T> rows) {
        PageImpl<T> impl = new PageImpl<>();
        impl.page = page == null ? 1 : page;
        impl.size = size == null ? 10 : size;
        impl.total = totalItem == null ? 0 : totalItem;
        impl.content = rows;
        return impl;
    }

    public static <T> Page<T> of(Pageable pageable, QueryResults<T> result) {
        PageImpl<T> impl = new PageImpl<>();
        impl.page = pageable.page();
        impl.size = pageable.size();
        impl.total = result.getTotal();
        impl.content = result.getResults();
        return impl;
    }

    public static <T> Page<T> of(Pageable pageable, QueryResults<Tuple> result, Function<Tuple, T> mapper) {
        PageImpl<T> impl = new PageImpl<>();
        impl.page = pageable.page();
        impl.size = pageable.size();
        impl.total = result.getTotal();
        impl.content = result.getResults().stream().map(mapper).collect(Collectors.toList());
        return impl;
    }

    public static <T, R> Page<R> of(Page<T> page, Function<T, R> mapper) {
        PageImpl<R> impl = new PageImpl<>();
        impl.page = page.getPage();
        impl.size = page.getSize();
        impl.total = page.getTotal();
        impl.content = page.getContent().stream().map(mapper).collect(Collectors.toList());
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
    public long getTotal() {
        return total;
    }

    @Override
    public List<T> getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "PageImpl{" +
                "page=" + page +
                ", size=" + size +
                ", total=" + total +
                ", content=" + content +
                '}';
    }
}
