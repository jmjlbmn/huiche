package org.huiche.core.util;

import com.querydsl.core.QueryResults;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLSerializer;
import org.huiche.core.dao.QueryDsl;
import org.huiche.core.page.PageRequest;
import org.huiche.core.page.PageResponse;

import java.util.List;

/**
 * @author Maning
 * @version 2017/8/9
 */
public class QueryDslUtil {

    public static <T> PageResponse<T> page(PageRequest pageRequest, SQLQuery<T> query) {
        if (null == pageRequest) {
            pageRequest = PageRequest.dft();
        }
        query.offset(pageRequest.getOffset());
        query.limit(pageRequest.getRows());
        SQLSerializer serializer = new SQLSerializer(QueryDsl.CONFIG);
        serializer.serialize(query.getMetadata(), true);
        QueryDsl.logSql(query.getMetadata(), serializer);
        QueryResults<T> results = query.fetchResults();
        return new PageResponse<T>().setRows(results.getResults()).setTotal(results.getTotal());
    }

    public static <T> List<T> listFromPage(PageRequest pageRequest, SQLQuery<T> query) {
        if (null == pageRequest) {
            pageRequest = PageRequest.dft();
        }
        query.offset(pageRequest.getOffset());
        query.limit(pageRequest.getRows());
        return query.fetch();
    }

    public static <T> List<T> list(SQLQuery<T> query) {
        return query.fetch();
    }

    public static long count(SQLQuery<?> query) {
        return query.fetchCount();
    }

    public static <T> T one(SQLQuery<T> query) {
        return query.fetchFirst();
    }


}
