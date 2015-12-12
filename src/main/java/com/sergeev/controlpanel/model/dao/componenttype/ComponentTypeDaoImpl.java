package com.sergeev.controlpanel.model.dao.componenttype;

import com.sergeev.controlpanel.model.ComponentType;
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
public class ComponentTypeDaoImpl implements ComponentTypeDao<ComponentType, Long> {

    @Autowired
    private SessionFactory sessionFactory;

    public ComponentTypeDaoImpl() {
    }

    public ComponentTypeDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override @Transactional
    public void persist(ComponentType entity) {
        getCurrentSession().save(entity);
    }

    @Override @Transactional
    public void update(ComponentType entity) {
        getCurrentSession().update(entity);
    }

    @Override @Transactional
    public ComponentType findById(Long aLong) {
        return getCurrentSession().get(ComponentType.class, aLong);
    }

    @Override @Transactional
    public void delete(ComponentType entity) {
        getCurrentSession().delete(entity);
    }

    @Override @Transactional @SuppressWarnings("unchecked")
    public Set<ComponentType> getAll() {
        List<ComponentType> componentList = (List<ComponentType>) getCurrentSession().createQuery("from ComponentType").list();
        Set<ComponentType> componentSet = new HashSet<>();
        componentList.forEach(n -> componentSet.add(n));
        return componentSet;
    }

    @Override @Transactional
    public void deleteAll() {
        Set<ComponentType> list = getAll();
        for(ComponentType componentType : list){
            delete(componentType);
        }
    }
}
