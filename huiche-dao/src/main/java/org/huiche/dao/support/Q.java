package org.huiche.dao;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.sql.SQLQuery;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
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

    public static Q of(@Nullable Predicate where) {
        return new Q().where(where);
    }

    public Q where(@Nullable Predicate where) {
        if (where != null) {
            this.wheres.add(where);
        }
        return this;
    }

    public static Q of(@Nullable OrderSpecifier<?> order) {
        return new Q().order(order);
    }

    public Q order(@Nullable OrderSpecifier<?> order) {
        if (order != null) {
            this.orders.add(order);
        }
        return this;
    }

    public static Q of(@NotNull Long page, @NotNull Long size) {
        return new Q().page(page, size);
    }

    public Q page(@NotNull Long page, @NotNull Long size) {
        this.page = page;
        this.size = size;
        return this;
    }

    public static Q of(@NotNull Predicate[] wheres) {
        return new Q().where(wheres);
    }

    public Q where(@NotNull Predicate[] wheres) {
        this.wheres.addAll(Arrays.asList(wheres));
        return this;
    }

    public static Q of(@NotNull OrderSpecifier<?>[] orders) {
        return new Q().order(orders);
    }

    public Q order(@NotNull OrderSpecifier<?>[] orders) {
        this.orders.addAll(Arrays.asList(orders));
        return this;
    }

    public Q size(@NotNull Long size) {
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

    public Q where(@Nullable Predicate where1, @Nullable Predicate where2) {
        addWhere(where1);
        addWhere(where2);
        return this;
    }

    private void addWhere(@Nullable Predicate where) {
        if (where != null) {
            this.wheres.add(where);
        }
    }

    public Q where(@Nullable Predicate where1, @Nullable Predicate where2, @Nullable Predicate where3) {
        addWhere(where1);
        addWhere(where2);
        addWhere(where3);
        return this;
    }

    public Q where(@Nullable Predicate where1, @Nullable Predicate where2, @Nullable Predicate where3, @Nullable Predicate where4) {
        addWhere(where1);
        addWhere(where2);
        addWhere(where3);
        addWhere(where4);
        return this;
    }

    public Q where(@Nullable Predicate where1, @Nullable Predicate where2, @Nullable Predicate where3, @Nullable Predicate where4, @Nullable Predicate where5) {
        addWhere(where1);
        addWhere(where2);
        addWhere(where3);
        addWhere(where4);
        addWhere(where5);
        return this;
    }

    public Q where(@Nullable Predicate where1, @Nullable Predicate where2, @Nullable Predicate where3, @Nullable Predicate where4, @Nullable Predicate where5, @NotNull Predicate... wheres) {
        addWhere(where1);
        addWhere(where2);
        addWhere(where3);
        addWhere(where4);
        addWhere(where5);
        this.wheres.addAll(Arrays.asList(wheres));
        return this;
    }

    public Q order(@Nullable OrderSpecifier<?> order1, @Nullable OrderSpecifier<?> order2) {
        if (order1 != null) {
            this.orders.add(order1);
        }
        if (order2 != null) {
            this.orders.add(order2);
        }
        return this;
    }

    public Q order(@Nullable OrderSpecifier<?> order1, @Nullable OrderSpecifier<?> order2, @NotNull OrderSpecifier<?>... orders) {
        if (order1 != null) {
            this.orders.add(order1);
        }
        if (order2 != null) {
            this.orders.add(order2);
        }
        this.orders.addAll(Arrays.asList(orders));
        return this;
    }
}
