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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dmitry-sergeev on 23.09.15.
 */
@Controller
public class AdminController {

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private UserDaoImpl userDao;

    @Autowired
    private NodeDaoImpl nodeDao;

    @RequestMapping(value = "/admin/user/{username}", method = RequestMethod.GET)
    @ResponseBody
    public User getUser(@PathVariable(value = "username") String username){
        LOG.debug("Received /admin/user GET for user={}...", username);

//        return Utils.constructJsonAnswer(userDao.findByUsername(username));
        return userDao.findByUsername(username);
    }

    @RequestMapping(value = "/admin/user", method = RequestMethod.POST,
            params = {"username", "password", "role"})
    @ResponseBody
    public ResponseEntity addUser(@RequestParam(value = "username") String username,
                                  @RequestParam(value = "password") String password,
                                  @RequestParam(value = "role") String role){
        LOG.debug("Received /admin/user POST for user={}...", username);

        User user = new User(username, new BCryptPasswordEncoder().encode(password), UserRole.valueOf(role), new HashSet<>(), true);
        userDao.persist(user);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", "ok");
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

    @RequestMapping(value = "/admin*", method = RequestMethod.GET)
    public String adminPage(Model model){
        LOG.debug("Received /admin request...");
        model.addAttribute("responseJson", "Restricted area!");
        return "/admin/index";
    }

    @RequestMapping(value = "/admin/nodes", method = RequestMethod.GET)
    @ResponseBody
    public Set<Node> getNodes() throws UnknownHostException {
        LOG.debug("Received /admin/nodes GET request");

        return nodeDao.getAll();
    }

    @RequestMapping(value = "/admin/node/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Node getNode(@PathVariable(value = "id") Long id)
            throws UnknownHostException{
        LOG.debug("Received /admin/node/ GET request for id {}...", id);

        return nodeDao.findById(id);
    }

    @RequestMapping(value = "/admin/user/permission", method = RequestMethod.POST,
            params = {"username", "role"})
    @ResponseBody
    public ResponseEntity changePermission(@RequestParam(value = "username") String username,
                                           @RequestParam(value = "role") String role){
        LOG.debug("Received /admin/user/permission POST for user={}...", username);


        User user = userDao.findByUsername(username);
        user.setRole(UserRole.valueOf(role));
        userDao.update(user);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", "ok");
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }


}
