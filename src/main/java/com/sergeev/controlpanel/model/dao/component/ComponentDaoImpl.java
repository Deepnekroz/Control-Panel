package com.sergeev.controlpanel.model.dao.component;

import com.sergeev.controlpanel.model.Component;
import com.sergeev.controlpanel.model.Node;
import com.sergeev.controlpanel.model.dao.user.UserDaoInterface;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by dmitry-sergeev on 22.09.15.
 */
@Repository
public class ComponentDaoImpl implements ComponentDaoInterface<Component, Long> {

    @Autowired
    private SessionFactory sessionFactory;

    public ComponentDaoImpl() {
    }

    public ComponentDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override @Transactional
    public void persist(Component entity) {
        getCurrentSession().save(entity);
    }

    @Override @Transactional
    public void update(Component entity) {
        getCurrentSession().update(entity);
    }

    @Override @Transactional
    public Component findById(Long aLong) {
        return getCurrentSession().get(Component.class, aLong);
    }

    @Override @Transactional
    public void delete(Component entity) {
        getCurrentSession().delete(entity);
    }

    @Override @Transactional @SuppressWarnings("unchecked")
    public Set<Component> getAll() {
        List<Component> componentList = (List<Component>) getCurrentSession().createQuery("from Component").list();
        Set<Component> componentSet = new HashSet<>();
        componentList.forEach(n -> componentSet.add(n));
        return componentSet;
    }

    @Override @Transactional
    public void deleteAll() {
        Set<Component> list = getAll();
        for(Component component : list){
            delete(component);
        }
    }
}
