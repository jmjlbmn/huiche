package org.huiche.core.web;

import org.huiche.core.entity.BaseEntity;
import org.huiche.core.service.BaseCrudService;

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
