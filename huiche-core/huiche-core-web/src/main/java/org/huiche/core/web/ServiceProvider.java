package org.huiche.core.web;

import org.huiche.core.entity.BaseEntity;
import org.huiche.core.service.BaseCrudService;

/**
 * Service提供者
 *
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
