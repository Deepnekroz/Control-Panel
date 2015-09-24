package com.sergeev.controlpanel.model.dao.node;

import com.sergeev.controlpanel.model.Node;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dmitry-sergeev on 22.09.15.
 */
@Repository
public class NodeDaoImpl implements NodeDaoInterface<Node, Long> {

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
    }

    @Override @Transactional
    public void update(Node entity) {
        getCurrentSession().update(entity);
    }

    @Override @Transactional
    public Node findById(Long aLong) {
        Node node = getCurrentSession().get(Node.class, aLong);
        Hibernate.initialize(node.getComponents()); //'cause of Lazy initialization we need to do it manually
        return node;
    }

    @Override @Transactional
    public void delete(Node entity) {
        getCurrentSession().delete(entity);
    }

    @Override @Transactional @SuppressWarnings("unchecked")
    public List<Node> getAll() {
        return (List<Node>)getCurrentSession().createQuery("from Node")
                                                .list()
                                                .parallelStream()
                                                .map(n -> {
                                                    Node node = (Node) n;
                                                    Hibernate.initialize(node.getComponents());
                                                    Hibernate.initialize(node.getUsers());
                                                    return n;
                                                }).collect(Collectors.toList());
    }

    @Override @Transactional
    public void deleteAll() {
        List<Node> list = getAll();
        for(Node node : list){
            delete(node);
        }
    }
}
