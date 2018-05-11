package org.huiche.core.service;

import org.huiche.core.query.Query;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author Maning
 */
@Transactional(rollbackFor = Exception.class, readOnly = true)
public class BaseServiceImpl implements BaseService, Query {
}
