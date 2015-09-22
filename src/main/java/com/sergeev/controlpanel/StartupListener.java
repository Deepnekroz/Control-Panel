package com.sergeev.controlpanel;

import com.sergeev.controlpanel.model.Node;
import com.sergeev.controlpanel.model.dao.user.UserDaoImpl;
import com.sergeev.controlpanel.model.user.User;
import com.sergeev.controlpanel.model.user.UserRole;
import com.sergeev.controlpanel.utils.PasswordEncoderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Created by dmitry-sergeev on 22.09.15.
 */
@Component
public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {



    @Autowired
    UserDaoImpl userDao;

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {

        PasswordEncoderImpl passwordEncoder = new PasswordEncoderImpl();
        userDao.persist(new User("admin", passwordEncoder.encode("12345"), UserRole.ADMIN, new ArrayList<Node>()));
    }
}
