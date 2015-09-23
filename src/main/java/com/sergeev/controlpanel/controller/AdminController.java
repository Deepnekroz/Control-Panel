package com.sergeev.controlpanel.controller;

import com.google.gson.JsonObject;
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

import java.util.ArrayList;

/**
 * Created by dmitry-sergeev on 23.09.15.
 */
@Controller
public class AdminController {

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private UserDaoImpl userDao;

    @RequestMapping(value = "/admin/user/{username}", method = RequestMethod.GET)
    @ResponseBody
    public String getUser(Model model,
                          @PathVariable(value = "username") String username){
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

        User user = new User(username, new BCryptPasswordEncoder().encode(password), UserRole.valueOf(role), new ArrayList<>(), true);
        userDao.persist(user);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", "ok");
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

    @RequestMapping(value = "/admin*", method = RequestMethod.GET)
    public String adminPage(Model model){
        LOG.debug("Received /admin request...");
        model.addAttribute("responseJson", "Restricted area!");
        return "redirect:/admin/index.html";
    }
}
