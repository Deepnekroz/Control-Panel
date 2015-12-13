package com.sergeev.controlpanel;

import com.sergeev.controlpanel.model.Node;
import com.sergeev.controlpanel.model.user.User;
import com.sergeev.controlpanel.model.dao.node.NodeDaoImpl;
import com.sergeev.controlpanel.model.dao.user.UserDaoImpl;
import com.sergeev.controlpanel.model.user.UserRole;
import junit.framework.Test;
import org.aspectj.apache.bcel.classfile.Unknown;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.Assert;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Set;

/**
 * Created by Agitolyev on 12.11.15.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {ApplicationContextConfig.class, AppSecurityConfig.class, MvcConf.class,
        SecurityInit.class, SpringWebAppInitializer.class })


public class UserDaoTest {

    @Autowired
    UserDaoImpl userDao;

    @Autowired
    NodeDaoImpl nodeDao;


    public void placeGetUserTest() throws UnknownHostException {

        InetAddress inetAddress = InetAddress.getByName("google.com");
        Node node = new Node("MyNode 1", inetAddress, "Debian", "1.4");
        nodeDao.persist(node);
        Set<Node> nodes = nodeDao.getAll();
        User user = new User("Test","qwerty",UserRole.ROLE_USER, nodes , true);
        userDao.persist(user);
        Set<User> users =  userDao.getAll();
        boolean isFounded =false;

        for(User u : users){
            if(u.getName().equals("Test") &&
                    u.getPassword().equals("qwerty") &&
                    u.getNodeList().equals(nodes) &&
                    u.getRole().equals(UserRole.ROLE_USER))
                isFounded = true;
        }

        Assert.isTrue(isFounded);

    }
}

