package org.huiche.dao.provider;

import org.huiche.dao.curd.*;
import org.huiche.data.entity.BaseEntity;
import org.huiche.data.query.Query;

/**
 * 基础数据库查询Dao
 *
 * @author Maning
 */
public interface BaseCrudDaoProvider<T extends BaseEntity<T>> extends
        CreateCmd<T>,
        CreatesCmd<T>,
        UpdateCmd<T>,
        UpdatesCmd<T>,
        DeleteCmd<T>,
        CountQuery<T>,
        ExistsQuery<T>,
        GetQuery<T>,
        GetColumnQuery<T>,
        GetColumnsQuery<T>,
        ListQuery<T>,
        ListColumnQuery<T>,
        ListColumnsQuery<T>,
        PageQuery<T>,
        PageColumnsQuery<T>,
        Query,
        SqlProvider {
}