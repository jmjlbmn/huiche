package org.huiche.dao.provider;

import org.huiche.dao.curd.CountQuery;
import org.huiche.dao.curd.CreateCmd;
import org.huiche.dao.curd.DeleteCmd;
import org.huiche.dao.curd.ExistsQuery;
import org.huiche.dao.curd.GetColumnQuery;
import org.huiche.dao.curd.GetColumnsQuery;
import org.huiche.dao.curd.GetQuery;
import org.huiche.dao.curd.ListColumnQuery;
import org.huiche.dao.curd.ListColumnsQuery;
import org.huiche.dao.curd.ListQuery;
import org.huiche.dao.curd.PageColumnsQuery;
import org.huiche.dao.curd.PageQuery;
import org.huiche.dao.curd.UpdateCmd;
import org.huiche.data.entity.BaseEntity;
import org.huiche.data.query.Query;

/**
 * 基础数据库查询Dao接口
 *
 * @author Maning
 */
public interface BaseCrudDaoProvider<T extends BaseEntity<T>> extends
        CreateCmd<T>,
        UpdateCmd<T>,
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
        SqlProvider,
        PathProvider<T>,
        CreateHandleProvider<T> {
}
