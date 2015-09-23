package com.sergeev.controlpanel.controller;

import com.google.gson.JsonObject;
import com.sergeev.controlpanel.model.Node;
import com.sergeev.controlpanel.model.dao.node.NodeDaoImpl;
import com.sergeev.controlpanel.model.dao.user.UserDaoImpl;
import com.sergeev.controlpanel.utils.Utils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by dmitry-sergeev on 02.09.15.
 */
@Controller
public class MainController {

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(MainController.class);

    //Spring Security see this :
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "registered", required = false) String registered) {

        ModelAndView model = new ModelAndView();
        if (error != null) {
            model.addObject("error", "Invalid username and password!");
        }

        if (logout != null) {
            model.addObject("msg", "You've been logged out successfully.");
        }

        if (registered != null) {
            model.addObject("registered", registered);
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
