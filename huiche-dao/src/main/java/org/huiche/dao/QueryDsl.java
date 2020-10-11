package org.huiche.dao;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.util.JdbcConstants;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ParamExpression;
import com.querydsl.core.types.ParamNotSetException;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.DB2Templates;
import com.querydsl.sql.DerbyTemplates;
import com.querydsl.sql.H2Templates;
import com.querydsl.sql.HSQLDBTemplates;
import com.querydsl.sql.MySQLTemplates;
import com.querydsl.sql.OracleTemplates;
import com.querydsl.sql.PostgreSQLTemplates;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.SQLListener;
import com.querydsl.sql.SQLSerializer;
import com.querydsl.sql.SQLServerTemplates;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.dml.SQLInsertBatch;
import com.querydsl.sql.dml.SQLMergeBatch;
import com.querydsl.sql.dml.SQLUpdateBatch;
import com.querydsl.sql.types.Null;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * QueryDsl配置,提供默认配置及Sql语句打印实现
 *
 * @author Maning
 */
@Slf4j
public class QueryDsl {
    public static Configuration CONFIG = new Configuration(SQLTemplates.DEFAULT);
    private static DbType type;

    public static void init(SQLTemplates templates) {
        CONFIG = new Configuration(templates);
        if (templates instanceof MySQLTemplates) {
            type = JdbcConstants.MYSQL;
        } else if (templates instanceof H2Templates) {
            type = JdbcConstants.H2;
        } else if (templates instanceof OracleTemplates) {
            type = JdbcConstants.ORACLE;
        } else if (templates instanceof PostgreSQLTemplates) {
            type = JdbcConstants.POSTGRESQL;
        } else if (templates instanceof DB2Templates) {
            type = JdbcConstants.DB2;
        } else if (templates instanceof DerbyTemplates) {
            type = JdbcConstants.DERBY;
        } else if (templates instanceof HSQLDBTemplates) {
            type = JdbcConstants.HSQL;
        } else if (templates instanceof SQLServerTemplates) {
            type = JdbcConstants.SQL_SERVER;
        } else {
            type = JdbcConstants.MYSQL;
        }
        CONFIG.setExceptionTranslator(new QueryDslExceptionTranslator());
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

    private static boolean druid = true;

    static {
        try {
            Class.forName("com.alibaba.druid.sql.SQLUtils");
        } catch (ClassNotFoundException e) {
            druid = false;
        }
    }

    public static void logSql(@Nonnull QueryMetadata md, @Nonnull SQLSerializer serializer) {
        List<Object> parameters = new ArrayList<>();
        Map<ParamExpression<?>, Object> params = md.getParams();
        for (Object o : serializer.getConstants()) {
            if (o instanceof ParamExpression) {
                if (!params.containsKey(o)) {
                    throw new ParamNotSetException((ParamExpression<?>) o);
                }
                o = md.getParams().get(o);
            }
            if (o instanceof Null) {
                parameters.add(null);
            } else {
                parameters.add(o);
            }
        }
        String sql = serializer.toString();
        if (druid) {
            try {
                sql = SQLUtils.format(sql, type, parameters);
            } catch (Exception ignored) {
                sql = parseParameters(sql, parameters);
            }
        } else {
            sql = parseParameters(sql, parameters);
        }
        log.debug(sql);
    }

    private static String parseParameters(String sql, List<Object> parameters) {
        if (!parameters.isEmpty()) {
            for (Object o : parameters) {
                if (null == o) {
                    sql = sql.replaceFirst("\\?", "NULL");
                } else if (o instanceof Number) {
                    sql = sql.replaceFirst("\\?", String.valueOf(o));
                } else {
                    sql = sql.replaceFirst("\\?", "'" + Matcher.quoteReplacement(String.valueOf(o)) + "'");
                }
            }
        }
        return sql;
    }
}
