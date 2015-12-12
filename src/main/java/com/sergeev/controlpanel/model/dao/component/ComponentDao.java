package com.sergeev.controlpanel.model.dao.component;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by dmitry-sergeev on 22.09.15.
 */
public interface ComponentDao<T, Id extends Serializable> {

    public void persist(T entity);

    public void update(T entity);

    public T findById(Id id);

    public void delete(T entity);

    public Set<T> getAll();

    public void deleteAll();
}
