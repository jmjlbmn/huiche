package org.huiche.core.web;

import org.huiche.core.entity.BaseEntity;
import org.huiche.core.service.BaseCrudService;

/**
 * @author Maning
 */
public interface ServiceProvider<T extends BaseEntity> {
    /**
     * 提供service
     *
     * @return service
     */
    BaseCrudService<T> service();
}
