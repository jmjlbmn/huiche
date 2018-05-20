package org.huiche.web;


import org.huiche.data.entity.BaseEntity;
import org.huiche.service.BaseCrudService;

import javax.annotation.Nonnull;

/**
 * Service提供者
 *
 * @author Maning
 */
public interface ServiceProvider<T extends BaseEntity<T>> {
    /**
     * 提供service
     *
     * @return service
     */
    @Nonnull
    BaseCrudService<T> service();
}
