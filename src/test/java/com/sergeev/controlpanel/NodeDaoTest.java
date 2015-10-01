package com.sergeev.controlpanel;

import com.sergeev.controlpanel.model.Node;
import com.sergeev.controlpanel.model.dao.node.NodeDaoImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.Assert;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;

/**
 * Created by dmitry-sergeev on 22.09.15.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {ApplicationContextConfig.class, AppSecurityConfig.class, MvcConf.class,
                                SecurityInit.class, SpringWebAppInitializer.class })
public class NodeDaoTest {

    @Autowired
    NodeDaoImpl nodeDao;

    @Test
    public void placeAndGetAllTest() throws UnknownHostException{
        InetAddress inetAddress = InetAddress.getByName("google.com");
        Node node = new Node("MyNode 1", inetAddress, "Debian", "1.4");
        nodeDao.persist(node);
        Set<Node> nodes = nodeDao.getAll();
        boolean isFounded = false;
        for(Node n : nodes){
            if("MyNode 1".equals(n.getName())
                    && n.getInetAddress().equals(inetAddress)
                    && "Debian".equals(n.getOsName())
                    && "1.4".equals(n.getOsVersion()))
                isFounded=true;
        }
        Assert.isTrue(isFounded);
    }
}
