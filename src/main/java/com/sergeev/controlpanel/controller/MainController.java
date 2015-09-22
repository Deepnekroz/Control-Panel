package com.sergeev.controlpanel.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sergeev.controlpanel.model.Node;
import com.sergeev.controlpanel.model.dao.node.NodeDaoImpl;
import com.sergeev.controlpanel.model.dao.user.UserDaoImpl;
import com.sergeev.controlpanel.model.dao.user.UserDaoInterface;
import com.sergeev.controlpanel.model.user.User;
import com.sergeev.controlpanel.model.user.UserRole;
import com.sergeev.controlpanel.utils.PasswordEncoderImpl;
import com.sergeev.controlpanel.utils.Utils;
import jdk.nashorn.internal.parser.JSONParser;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dmitry-sergeev on 02.09.15.
 */
@Controller
public class MainController {

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(MainController.class);

    @Autowired
    private NodeDaoImpl nodeDao;

    @Autowired
    private UserDaoImpl userDao;

    PasswordEncoder passwordEncoder = new PasswordEncoderImpl();

    @RequestMapping(value = "/node", method = RequestMethod.POST,
                    params = {"name", "inetaddr", "osName", "osVersion"})
    @ResponseBody
    public ResponseEntity addNode(Model model,
                           @RequestParam(value = "name") String name,
                           @RequestParam(value = "inetaddr") String inetaddr,
                           @RequestParam(value = "osName") String osName,
                           @RequestParam(value = "osVersion") String osVersion)
                            throws UnknownHostException{
        LOG.debug("Received /node POST request...");

        Node node = new Node(name, InetAddress.getByName(inetaddr), osName, osVersion);
        nodeDao.persist(node);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", "ok");

        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

    @RequestMapping(value = "/node", method = RequestMethod.GET, params = {"id"})
    @ResponseBody
    public String getNode(Model model,
                          @RequestParam(value = "id") Long id)
                                        throws UnknownHostException{
        LOG.debug("Received /node GET request for id {}...", id);

        return Utils.constructJsonAnswer(nodeDao.findById(id));
    }

    @RequestMapping(value = "/nodes", method = RequestMethod.GET)
    @ResponseBody
    public String getNodes(Model model) throws UnknownHostException{
        LOG.debug("Received /nodes GET request for");

        return Utils.constructJsonAnswer(nodeDao.getAll());
    }

    @RequestMapping(value = "/admin/user", method = RequestMethod.GET, params = {"username"})
    @ResponseBody
    public String getUser(Model model,
                          @RequestParam(value = "username") String username){
        LOG.debug("Received /admin/user for user={}...", username);

        return Utils.constructJsonAnswer(userDao.findByUsername(username));
    }

    @RequestMapping(value = "/admin/user", method = RequestMethod.POST,
                    params = {"username", "password", "role", "nodes"})
    @ResponseBody
    public ResponseEntity addUser(Model model,
                          @RequestParam(value = "username") String username,
                          @RequestParam(value = "password") String password,
                          @RequestParam(value = "role") String role){
        LOG.debug("Received /admin/user for user={}...", username);

        User user = new User(username, passwordEncoder.encode(password), UserRole.valueOf(role), null);
        userDao.persist(user);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", "ok");
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminPage(Model model){
        LOG.debug("Received /admin request...");
        model.addAttribute("responseJson", "Restricted area!");
        return "admin/admin";
    }

    //Spring Security see this :
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout) {

        ModelAndView model = new ModelAndView();
        if (error != null) {
            model.addObject("error", "Invalid username and password!");
        }

        if (logout != null) {
            model.addObject("msg", "You've been logged out successfully.");
        }
        model.setViewName("login");

        return model;

    }


    @ExceptionHandler(Exception.class) @ResponseBody
    public String handleAllException(Exception ex) {
        JsonObject jsonObject = new JsonObject();

        if(ex instanceof UnknownHostException){
            jsonObject.addProperty("status", "400");
            jsonObject.addProperty("message", "Unknown host address given");
        }else{
            jsonObject.addProperty("status", "400");
            jsonObject.addProperty("message", ex.toString());
        }

        return jsonObject.toString();
    }
}
