package org.huiche.dao.provider;

/**
 * @author Maning
 */
public interface UpdateHandleProvider<T> {
    void validRegular(T entity);

    void beforeUpdate(T entity);
}
