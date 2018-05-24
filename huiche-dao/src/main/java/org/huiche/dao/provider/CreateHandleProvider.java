package org.huiche.dao.provider;

/**
 * @author Maning
 */
public interface CreateHandleProvider<T> extends UpdateHandleProvider<T> {
    void validOnCreate(T entity);

    void beforeCreate(T entity);
    boolean doSetId();
}
