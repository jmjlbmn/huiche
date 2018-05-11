package org.huiche.core.dao;

import com.querydsl.sql.SQLQueryFactory;

import javax.annotation.Resource;

/**
 * @author Maning
 */
public class CommonDao implements Query{
    @Resource
    protected SQLQueryFactory sqlQueryFactory;
}
