package org.huiche.dao;

import com.querydsl.core.types.Path;
import com.querydsl.sql.PrimaryKey;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.SQLQueryFactory;
import org.huiche.dao.operation.CrudOperation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Maning
 */
public class CrudDaoFactory {
    @NotNull
    public static <T> CrudOperation<T> create(SQLQueryFactory sql, @NotNull RelationalPath<T> table) {
        List<IdInfo> ids = getIdListByTable(table);
        int size = ids.size();
        if (size == 0) {
            return new CrudDaoNonePkImpl<>(sql, table);
        } else if (size == 1) {
            return new CrudDaoSinglePkImpl<>(sql, table, ids.get(0));
        } else {
            return new CrudDaoMultiplePkImpl<>(sql, table, ids);
        }
    }

    @NotNull
    public static <T> List<IdInfo> getIdListByTable(@NotNull RelationalPath<T> table) {
        PrimaryKey<T> pk = table.getPrimaryKey();
        if (pk != null) {
            List<? extends Path<?>> ids = pk.getLocalColumns();
            int size = ids.size();
            if (size > 0) {
                List<IdInfo> list = new ArrayList<>(size);
                for (Path<?> id : ids) {
                    list.add(new IdInfo(table, id));
                }
                return list;
            }
        }
        return Collections.emptyList();
    }
}
