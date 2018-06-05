package org.huiche.service;

import org.huiche.data.query.Query;
import org.springframework.transaction.annotation.Transactional;


/**
 * 基础业务实现,提供了只读事务(QueryDsl数据查询需要),所有业务方法都应该继承此方法
 *
 * @author Maning
 */
@Transactional(rollbackFor = Exception.class, readOnly = true)
public class BaseServiceImpl implements BaseService, Query {
}
