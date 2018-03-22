package org.huiche.core.api;

import org.huiche.core.entity.BaseEntity;
import org.huiche.core.service.BaseService;

/**
 * @author Maning
 */
public interface Api<T extends BaseEntity> {
    /**
     * service
     *
     * @return service
     */
    BaseService<T> service();
}
