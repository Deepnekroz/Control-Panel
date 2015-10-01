package com.sergeev.controlpanel.model.dao.node;

import com.sergeev.controlpanel.model.Node;
import com.sergeev.controlpanel.model.dao.user.UserDaoInterface;
import com.sergeev.controlpanel.model.user.User;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by dmitry-sergeev on 22.09.15.
 */
@Repository
public class NodeDaoImpl implements NodeDaoInterface<Node, Long> {

    @Autowired
    UserDaoInterface userDao;

    @Autowired
    private SessionFactory sessionFactory;

    public NodeDaoImpl() {
    }

    public NodeDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override @Transactional
    public void persist(Node entity) {
        getCurrentSession().save(entity);
        entity.getUsers().forEach(userDao::update);
    }

    @Override @Transactional
    public void update(Node entity) {
        getCurrentSession().update(entity);
    }

    @Override @Transactional
    public Node findById(Long aLong) {
        return getCurrentSession().get(Node.class, aLong);
    }

    @Override @Transactional
    public void delete(Node entity) {
        getCurrentSession().delete(entity);
    }

    @Override @Transactional @SuppressWarnings("unchecked")
    public Set<Node> getAll() {
        List<Node> nodesList = (List<Node>)getCurrentSession().createQuery("from Node").list();
        Set<Node> nodesSet = new HashSet<>();
        nodesList.forEach(n -> nodesSet.add(n));
        return nodesSet;
    }

    @Override @Transactional
    public void deleteAll() {
        Set<Node> list = getAll();
        for(Node node : list){
            delete(node);
        }
    }
}
