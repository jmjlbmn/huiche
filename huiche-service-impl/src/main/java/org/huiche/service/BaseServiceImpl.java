package org.huiche.service;

import org.huiche.data.query.Query;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author Maning
 */
@Transactional(rollbackFor = Exception.class, readOnly = true)
public class BaseServiceImpl implements BaseService, Query {
}
