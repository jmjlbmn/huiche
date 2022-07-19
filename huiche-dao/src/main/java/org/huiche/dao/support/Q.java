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
public class Q {
    private final List<Predicate> wheres = new ArrayList<>();
    private final List<OrderSpecifier<?>> orders = new ArrayList<>();
    private Long page;
    private Long size;

    public static Q of() {
        return new Q();
    }

    public static Q of(Long page, Long size) {
        return new Q().page(page, size);
    }

    public Q page(Long page, Long size) {
        this.page = page;
        this.size = size;
        return this;
    }

    public static Q of(Pageable pageable) {
        return new Q().page(pageable);
    }

    public Q page(@NonNull Pageable pageable) {
        this.page = pageable.page();
        this.size = pageable.size();
        return this;
    }

    public static Q of(Predicate... wheres) {
        return new Q().where(wheres);
    }

    public static Q of(Query query) {
        return new Q().where(query.get());
    }

    public Q where(@NonNull Predicate... wheres) {
        for (Predicate where : wheres) {
            if (where != null) {
                this.wheres.add(where);
            }
        }
        return this;
    }

    public Q where(@NonNull Query query) {
        return where(query.get());
    }

    public static Q of(OrderSpecifier<?>... orders) {
        return new Q().order(orders);
    }

    public Q order(@NonNull OrderSpecifier<?>... orders) {
        for (OrderSpecifier<?> order : orders) {
            if (order != null) {
                this.orders.add(order);
            }
        }
        return this;
    }

    public Q size(Long size) {
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
