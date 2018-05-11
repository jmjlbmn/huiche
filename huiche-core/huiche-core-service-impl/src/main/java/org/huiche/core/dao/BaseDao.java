package org.huiche.core.dao;

import com.querydsl.sql.SQLQueryFactory;
import org.huiche.core.query.Query;

import javax.annotation.Resource;

/**
 * @author Maning
 */
public class BaseDao implements Query {
    @Resource
    protected SQLQueryFactory sqlQueryFactory;
}
