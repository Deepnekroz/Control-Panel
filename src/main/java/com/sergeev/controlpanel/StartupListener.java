package com.sergeev.controlpanel;

import com.sergeev.controlpanel.model.dao.node.NodeDao;
import com.sergeev.controlpanel.model.dao.user.UserDao;
import com.sergeev.controlpanel.model.user.User;
import com.sergeev.controlpanel.model.user.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;

/**
 * Created by dmitry-sergeev on 22.09.15.
 */
@Component
public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    UserDao userDao;

    @Autowired
    NodeDao nodeDao;

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event){
        userDao.persist(new User("admin", new BCryptPasswordEncoder().encode("12345"), UserRole.ROLE_ADMIN, new HashSet<>(), true));
        userDao.persist(new User("user", new BCryptPasswordEncoder().encode("12345"), UserRole.ROLE_USER, new HashSet<>(), true));
    }
}
