package com.sergeev.controlpanel.model.dao.user;

import com.sergeev.controlpanel.model.user.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by dmitry-sergeev on 22.09.15.
 */
@Repository
public class UserDaoImpl implements UserDao<User, Long> {

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
        return getCurrentSession().get(User.class, aLong);
    }

    @Override @Transactional
    public User findByUsername(String username) {
        List<User> users = getCurrentSession().createQuery("from User where name=?").setParameter(0, username).list();
        LOG.debug("Users with name: " + username + Arrays.toString(users.toArray()));
        if(users.size()<1)
            return null;
        return users.get(0);
    }

    @Override @Transactional
    public void delete(User entity) {
        getCurrentSession().delete(entity);
    }

    @Override @Transactional @SuppressWarnings("unchecked")
    public Set<User> getAll() {

        List<User> userList = (List<User>) getCurrentSession().createQuery("from User").list();
        Set<User> userSet = new HashSet<>();
        userList.forEach(n -> userSet.add(n));

        return userSet;
    }

    @Override @Transactional
    public void deleteAll() {
        Set<User> list = getAll();
        for(User user : list){
            delete(user);
        }
    }
}
