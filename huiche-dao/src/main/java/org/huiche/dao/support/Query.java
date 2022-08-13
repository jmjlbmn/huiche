package org.huiche.dao.support;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.sql.SQLQuery;
import org.huiche.domain.PageRequest;
import org.huiche.domain.Pageable;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Maning
 */
public class Query {
    private final List<Predicate> wheres = new ArrayList<>();
    private final List<OrderSpecifier<?>> orders = new ArrayList<>();
    private Long page;
    private Long size;

    public static Query of() {
        return new Query();
    }

    public static Query of(Long page, Long size) {
        return new Query().page(page, size);
    }

    public Query page(Long page, Long size) {
        this.page = page;
        this.size = size;
        return this;
    }

    public static Query of(Pageable pageable) {
        return new Query().page(pageable);
    }

    public Query page(@NonNull Pageable pageable) {
        this.page = pageable.page();
        this.size = pageable.size();
        return this;
    }

    public static Query of(Predicate... wheres) {
        return new Query().where(wheres);
    }

    public static Query of(Search search) {
        return new Query().where(search.get());
    }

    public Query where(@NonNull Predicate... wheres) {
        for (Predicate where : wheres) {
            if (where != null) {
                this.wheres.add(where);
            }
        }
        return this;
    }

    public Query where(@NonNull Search search) {
        return where(search.get());
    }

    public static Query of(OrderSpecifier<?>... orders) {
        return new Query().order(orders);
    }

    public Query order(@NonNull OrderSpecifier<?>... orders) {
        for (OrderSpecifier<?> order : orders) {
            if (order != null) {
                this.orders.add(order);
            }
        }
        return this;
    }

    public Query size(Long size) {
        this.size = size;
        return this;
    }

    public <T> SQLQuery<T> useForPage(SQLQuery<T> query) {
        use(query);
        if (size == null) {
            query.limit(10);
        }
        return query;
    }

    public <T> SQLQuery<T> use(SQLQuery<T> query) {
        if (!wheres.isEmpty()) {
            query.where(wheres.toArray(new Predicate[0]));
        }
        if (!orders.isEmpty()) {
            query.orderBy(orders.toArray(new OrderSpecifier<?>[0]));
        }
        if (page != null && size != null) {
            query.offset(size * (page - 1));
        }
        if (size != null) {
            query.limit(size);
        }
        return query;
    }

    public Pageable getPageable() {
        PageRequest pageable = new PageRequest();
        pageable.setPage(page == null ? 1 : page);
        pageable.setSize(size == null ? 10 : size);
        return pageable;
    }
}
