package com.sergeev.controlpanel.model.dao.user;

import com.sergeev.controlpanel.model.user.User;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dmitry-sergeev on 22.09.15.
 */
@Repository
public class UserDaoImpl implements UserDaoInterface<User, Long> {

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(UserDaoImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    public UserDaoImpl() {
    }

    public UserDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override @Transactional
    public void persist(User entity) {
        getCurrentSession().save(entity);
    }

    @Override @Transactional
    public void update(User entity) {
        getCurrentSession().update(entity);
    }

    @Override @Transactional
    public User findById(Long aLong) {
        User user = getCurrentSession().get(User.class, aLong);
        Hibernate.initialize(user.getNodeList()); //'cause of Lazy initialization we need to do it manually
        return user;
    }

    @Override @Transactional
    public User findByUsername(String username) {
        List<User> users = getCurrentSession().createQuery("from User where name=?").setParameter(0, username).list();
        LOG.debug("Users with name: " + username + Arrays.toString(users.toArray()));

        User user = users.get(0);
        if(user!=null)
            Hibernate.initialize(user.getNodeList()); //'cause of Lazy initialization we need to do it manually
        return user;
    }

    @Override @Transactional
    public void delete(User entity) {
        getCurrentSession().delete(entity);
    }

    @Override @Transactional @SuppressWarnings("unchecked")
    public List<User> getAll() {
        return (List<User>)getCurrentSession().createQuery("from User")
                .list()
                .parallelStream()
                .map(n -> {
                    Hibernate.initialize(((User) n).getNodeList());
                    return n;
                }).collect(Collectors.toList());
    }

    @Override @Transactional
    public void deleteAll() {
        List<User> list = getAll();
        for(User user : list){
            delete(user);
        }
    }
}
