package com.sergeev.controlpanel.model.dao.user;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dmitry-sergeev on 22.09.15.
 */
public interface UserDaoInterface<T, Id extends Serializable> {

    public void persist(T entity);

    public void update(T entity);

    public T findById(Id id);

    public T findByUsername(String username);

    public void delete(T entity);

    public List<T> getAll();

    public void deleteAll();
}
