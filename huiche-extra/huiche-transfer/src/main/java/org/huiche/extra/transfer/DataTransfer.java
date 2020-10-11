package org.huiche.extra.transfer;

import com.querydsl.core.types.Predicate;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.dml.DefaultMapper;
import com.querydsl.sql.dml.SQLInsertClause;
import org.huiche.dao.QueryDsl;
import org.huiche.dao.util.QueryUtil;

import javax.sql.DataSource;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Function;

/**
 * 数据迁移工具
 *
 * @author Maning
 */
public class DataTransfer {
    /**
     * 源数据库 Factory
     */
    private SQLQueryFactory sourceSqlQueryFactory;
    /**
     * 目标数据库 Factory
     */
    private SQLQueryFactory targetSqlQueryFactory;

    /**
     * 数据迁移工具构造
     *
     * @param sourceSqlQueryFactory 源数据库 Factory
     * @param targetSqlQueryFactory 目标数据库 Factory
     */
    public DataTransfer(SQLQueryFactory sourceSqlQueryFactory, SQLQueryFactory targetSqlQueryFactory) {
        this.sourceSqlQueryFactory = sourceSqlQueryFactory;
        this.targetSqlQueryFactory = targetSqlQueryFactory;
    }

    /**
     * 数据迁移构造
     *
     * @param srcUrl  源数据库 jdbc url
     * @param srcUser 源数据库 用户名
     * @param srcPwd  源数据库 密码
     * @param tgtUrl  目标数据库 jdbc url
     * @param tgtUser 目标数据库 用户名
     * @param tgtPwd  目标数据库 密码
     */
    public DataTransfer(String srcUrl, String srcUser, String srcPwd, String tgtUrl, String tgtUser, String tgtPwd) {
        sourceSqlQueryFactory = new SQLQueryFactory(QueryDsl.CONFIG, () -> {
            try {
                return DriverManager.getConnection(srcUrl, srcUser, srcPwd);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("源数据库连接失败", e);
            }
        });
        targetSqlQueryFactory = new SQLQueryFactory(QueryDsl.CONFIG, () -> {
            try {
                return DriverManager.getConnection(tgtUrl, tgtUser, tgtPwd);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("目标数据库连接失败", e);
            }
        });
    }

    /**
     * 数据迁移构造
     *
     * @param srcDataSource 源数据库连接池
     * @param tgtDataSource 目标数据库连接池
     */
    public DataTransfer(DataSource srcDataSource, DataSource tgtDataSource) {
        sourceSqlQueryFactory = new SQLQueryFactory(QueryDsl.CONFIG, srcDataSource);
        targetSqlQueryFactory = new SQLQueryFactory(QueryDsl.CONFIG, tgtDataSource);
    }

    /**
     * 数据迁移构造
     *
     * @param srcUrl        源数据库 jdbc url
     * @param srcUser       源数据库 用户名
     * @param srcPwd        源数据库 密码
     * @param tgtDataSource 目标数据库连接池
     */
    public DataTransfer(String srcUrl, String srcUser, String srcPwd, DataSource tgtDataSource) {
        sourceSqlQueryFactory = new SQLQueryFactory(QueryDsl.CONFIG, () -> {
            try {
                return DriverManager.getConnection(srcUrl, srcUser, srcPwd);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("源数据库连接失败", e);
            }
        });
        targetSqlQueryFactory = new SQLQueryFactory(QueryDsl.CONFIG, tgtDataSource, false);
    }

    /**
     * 数据迁移
     *
     * @param srcTablePath    源表 path
     * @param targetTablePath 目标表 path
     * @param mapper          数据转换mapper
     * @param <S>             源类型
     * @param <T>             目标类型
     */
    public <S, T> void transfer
    (RelationalPath<S> srcTablePath, RelationalPath<T> targetTablePath, Function<S, T> mapper) {
        transfer(srcTablePath, null, targetTablePath, mapper, 100);
    }

    /**
     * 数据迁移
     *
     * @param srcTablePath    源表 path
     * @param targetTablePath 目标表 path
     * @param mapper          数据转换mapper
     * @param size            每次处理多少条数据
     * @param <S>             源类型
     * @param <T>             目标类型
     */
    public <S, T> void transfer
    (RelationalPath<S> srcTablePath, RelationalPath<T> targetTablePath, Function<S, T> mapper, int size) {
        transfer(srcTablePath, null, targetTablePath, mapper, size);
    }

    /**
     * 数据迁移
     *
     * @param srcTablePath      源表 path
     * @param srcQueryPredicate 源表过滤条件
     * @param targetTablePath   目标表 path
     * @param mapper            数据转换mapper
     * @param <S>               源类型
     * @param <T>               目标类型
     */
    public <S, T> void transfer(RelationalPath<S> srcTablePath, Predicate
            srcQueryPredicate, RelationalPath<T> targetTablePath, Function<S, T> mapper) {
        transfer(srcTablePath, srcQueryPredicate, targetTablePath, mapper, 100);
    }

    /**
     * @param srcTablePath      源表 path
     * @param srcQueryPredicate 源表过滤条件
     * @param targetTablePath   目标表 path
     * @param mapper            数据转换mapper
     * @param size              每次处理多少条数据
     * @param <S>               源类型
     * @param <T>               目标类型
     */

    public <S, T> void transfer(RelationalPath<S> srcTablePath, Predicate
            srcQueryPredicate, RelationalPath<T> targetTablePath, Function<S, T> mapper, int size) {
        SQLQuery<S> query = sourceSqlQueryFactory.selectFrom(srcTablePath);
        if (null != srcQueryPredicate) {
            query = query.where(srcQueryPredicate);
        }
        long count = QueryUtil.count(query);
        if (0 == count) {
            System.out.println("原数据库无数据,无需导入");
            return;
        }
        SQLInsertClause insert = targetSqlQueryFactory.insert(targetTablePath);
        long totalPage = count % size == 0 ? count / size : count / size + 1;
        System.out.println("开始处理,共 " + count + " 条数据,进行 " + totalPage + " 次批量插入处理,请等待...");
        int fc = (int) Math.ceil(Math.log10(count));
        int fp = (int) Math.ceil(Math.log10(totalPage));
        fc = 0 == fc ? 1 : fc;
        fp = 0 == fp ? 1 : fp;
        for (long page = 1; page <= totalPage; page++) {
            long start = (page - 1) * size;
            long end = Math.min((start + size), count);
            System.out.println(String.format("%" + fp + "d", page) + "/" + totalPage +
                    " 已处理" + String.format("%5s", String.format("%.2f", start * 100 / (double) count)) +
                    "% 准备处理第" + String.format("%" + fc + "d", (start + 1)) +
                    " ~ " + String.format("%" + fc + "d", (end)) + "条数据");
            query.offset(start)
                    .limit(size)
                    .fetch()
                    .forEach(s -> insert.populate(mapper.apply(s), DefaultMapper.WITH_NULL_BINDINGS)
                            .addBatch());
            try {
                insert.execute();
                insert.clear();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("处理完成.");
    }
}
