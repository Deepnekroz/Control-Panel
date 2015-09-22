package com.sergeev.controlpanel.model.dao.node;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dmitry-sergeev on 22.09.15.
 */
public interface NodeDaoInterface<T, Id extends Serializable> {

    public void persist(T entity);

    public void update(T entity);

    public T findById(Id id);

    public void delete(T entity);

    public List<T> getAll();

    public void deleteAll();
}
