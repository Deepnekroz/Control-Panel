package com.sergeev.controlpanel.controller;

import com.google.gson.JsonObject;
import com.sergeev.controlpanel.model.Node;
import com.sergeev.controlpanel.model.dao.node.NodeDaoImpl;
import com.sergeev.controlpanel.model.dao.user.UserDaoImpl;
import com.sergeev.controlpanel.model.user.User;
import com.sergeev.controlpanel.model.user.UserRole;
import com.sergeev.controlpanel.utils.Utils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by dmitry-sergeev on 23.09.15.
 */
@Controller
public class UserController {

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(MainController.class);

    @Autowired
    private UserDaoImpl userDao;

    @Autowired
    private NodeDaoImpl nodeDao;


    @RequestMapping(value = "/adduser", method = RequestMethod.POST,
            params = {"username", "password"})
    public ModelAndView addUser(  @RequestParam(value = "username") String username,
                                  @RequestParam(value = "password") String password){
        LOG.debug("Received /user POST for user={}...", username);

        ModelAndView model = new ModelAndView();

        if(userDao.findByUsername(username) != null){
            model.addObject("error_register", "User with this name is already registered");
            model.setView(new RedirectView("login#toregister", true));
            return model;
        }

        User user = new User(username, new BCryptPasswordEncoder().encode(password), UserRole.ROLE_USER, new ArrayList<>(), true);

        userDao.persist(user);

        model.addObject("registered", "Successfully registered user " + username);
        model.setView(new RedirectView("login#tologin", true));

        return model;
    }

    @RequestMapping(value = "/user/isAdmin", method = RequestMethod.GET)
    @ResponseBody
    public String welcome() throws UnknownHostException {
        LOG.debug("Received /user/isAdmin GET request");
        for(Iterator iter = SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator(); iter.hasNext();){
            String currentRole = ((SimpleGrantedAuthority)iter.next()).getAuthority();
            if(UserRole.ROLE_ADMIN.name().equals(currentRole))
                return "{\"isAdmin\":\"true\"}";
        }
        return "{\"isAdmin\":\"false\"}";
    }

    /*
    Returns all nodes attached to current user
     */
    @RequestMapping(value = "/user/nodes", method = RequestMethod.GET)
    @ResponseBody
    public String getNodes() throws UnknownHostException{
        LOG.debug("Received /nodes GET request");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //get logged in username

        User user = userDao.findByUsername(username);

        return Utils.constructJsonAnswer(user.getNodeList());
    }

    @RequestMapping(value = "/user/node", method = RequestMethod.POST,
            params = {"name", "inetaddr", "osName", "osVersion"})
    @ResponseBody
    public ResponseEntity addNode(@RequestParam(value = "name") String name,
                                  @RequestParam(value = "inetaddr") String inetaddr,
                                  @RequestParam(value = "osName") String osName,
                                  @RequestParam(value = "osVersion") String osVersion)
            throws UnknownHostException{
        LOG.debug("Received /user/node POST request...");

        Node node = new Node(name, InetAddress.getByName(inetaddr), osName, osVersion);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        node.addUser(userDao.findByUsername(username));

        nodeDao.persist(node);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", "ok");

        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }
}
