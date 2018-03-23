package org.huiche.core.api.base;

import org.huiche.core.entity.BaseEntity;
import org.huiche.core.service.BaseService;

/**
 * @author Maning
 */
public interface ServiceProvider<T extends BaseEntity> {
    /**
     * 提供service
     *
     * @return service
     */
    BaseService<T> service();
}
