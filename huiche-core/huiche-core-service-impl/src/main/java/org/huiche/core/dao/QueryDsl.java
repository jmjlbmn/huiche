package org.huiche.core.dao;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.types.*;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.SQLListener;
import com.querydsl.sql.SQLSerializer;
import com.querydsl.sql.dml.SQLInsertBatch;
import com.querydsl.sql.dml.SQLMergeBatch;
import com.querydsl.sql.dml.SQLUpdateBatch;
import com.querydsl.sql.spring.SpringExceptionTranslator;
import com.querydsl.sql.types.Null;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * QueryDsl配置,事务管理器已经自动配置,不再手动配置
 *
 * @author Maning
 * @version 2017/11/2
 */
public class QueryDsl {
    private static final Logger LOG = LoggerFactory.getLogger(QueryDsl.class);
    public static final Configuration CONFIG;

    static {
        CONFIG = new Configuration(new MySqlExTemplates());
        CONFIG.setExceptionTranslator(new SpringExceptionTranslator());
        CONFIG.addListener(new SQLListener() {
            @Override
            public void notifyQuery(QueryMetadata md) {
                SQLSerializer serializer = new SQLSerializer(CONFIG);
                serializer.serialize(md, false);
                logSql(md, serializer);
            }

            @Override
            public void notifyDelete(RelationalPath<?> entity, QueryMetadata md) {
                SQLSerializer serializer = new SQLSerializer(CONFIG);
                serializer.serializeDelete(md, entity);
                logSql(md, serializer);
            }

            @Override
            public void notifyDeletes(RelationalPath<?> entity, List<QueryMetadata> batches) {
                if (!batches.isEmpty()) {
                    for (QueryMetadata md : batches) {
                        SQLSerializer serializer = new SQLSerializer(CONFIG);
                        serializer.serializeDelete(md, entity);
                        logSql(md, serializer);
                    }
                }
            }

            @Override
            public void notifyMerge(RelationalPath<?> entity, QueryMetadata md, List<Path<?>> keys, List<Path<?>> columns, List<Expression<?>> values, SubQueryExpression<?> subQuery) {
                SQLSerializer serializer = new SQLSerializer(CONFIG);
                serializer.serializeMerge(md, entity, keys, columns, values, subQuery);
                logSql(md, serializer);
            }

            @Override
            public void notifyMerges(RelationalPath<?> entity, QueryMetadata md, List<SQLMergeBatch> batches) {
                if (!batches.isEmpty()) {
                    for (SQLMergeBatch batch : batches) {
                        SQLSerializer serializer = new SQLSerializer(CONFIG);
                        serializer.serializeMerge(md, entity, batch.getKeys(), batch.getColumns(), batch.getValues(), batch.getSubQuery());
                        logSql(md, serializer);
                    }
                }
            }

            @Override
            public void notifyInsert(RelationalPath<?> entity, QueryMetadata md, List<Path<?>> columns, List<Expression<?>> values, SubQueryExpression<?> subQuery) {
                SQLSerializer serializer = new SQLSerializer(CONFIG);
                serializer.serializeInsert(md, entity, columns, values, subQuery);
                logSql(md, serializer);

            }

            @Override
            public void notifyInserts(RelationalPath<?> entity, QueryMetadata md, List<SQLInsertBatch> batches) {
                SQLSerializer serializer = new SQLSerializer(CONFIG);
                serializer.serializeInsert(md, entity, batches);
                logSql(md, serializer);
            }

            @Override
            public void notifyUpdate(RelationalPath<?> entity, QueryMetadata md, Map<Path<?>, Expression<?>> updates) {
                SQLSerializer serializer = new SQLSerializer(CONFIG);
                serializer.serializeUpdate(md, entity, updates);
                logSql(md, serializer);
            }

            @Override
            public void notifyUpdates(RelationalPath<?> entity, List<SQLUpdateBatch> batches) {
                if (!batches.isEmpty()) {
                    for (SQLUpdateBatch batch : batches) {
                        SQLSerializer serializer = new SQLSerializer(CONFIG);
                        serializer.serializeUpdate(batch.getMetadata(), entity, batch.getUpdates());
                        logSql(batch.getMetadata(), serializer);
                    }
                }
            }
        });
    }

    public static void logSql(QueryMetadata md, SQLSerializer serializer) {
        ImmutableList.Builder<Object> args = ImmutableList.builder();
        Map<ParamExpression<?>, Object> params = md.getParams();
        for (Object o : serializer.getConstants()) {
            if (o instanceof ParamExpression) {
                if (!params.containsKey(o)) {
                    throw new ParamNotSetException((ParamExpression<?>) o);
                }
                o = md.getParams().get(o);
            }
            args.add(o);
        }
        String sql = serializer.toString();
        ImmutableList<Object> list = args.build();
        if (!list.isEmpty()) {
            for (Object o : list) {
                if (o instanceof Number) {
                    sql = sql.replaceFirst("\\?", String.valueOf(o));
                } else if (o instanceof Null) {
                    sql = sql.replaceFirst("\\?", "NULL");
                } else {
                    sql = sql.replaceFirst("\\?", "'" + String.valueOf(o) + "'");
                }
            }
        }
        LOG.debug(sql);
    }
}
