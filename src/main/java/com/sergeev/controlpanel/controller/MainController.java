package com.sergeev.controlpanel.controller;

import com.google.gson.JsonObject;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Created by dmitry-sergeev on 02.09.15.
 */
@Controller
public class MainController {

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(MainController.class);

//    @Autowired
//    private UserDao userDao;

    @RequestMapping(value = "/welcome", method = RequestMethod.GET, params = {"param"})
    public void getWelcome(Model model,
                           @RequestParam(value = "param") String param){
        LOG.debug("Received /welcome request...");

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("parameter", param);
        model.addAttribute("responseJson", jsonObject.toString());
    }

    @RequestMapping(value = "/admin**", method = RequestMethod.GET)
    public String adminPage(Model model){
        LOG.debug("Received /admin request...");


        model.addAttribute("responseJson", "Restricted area!");
        return "admin/admin";
    }


    @ExceptionHandler(Exception.class) @ResponseBody
    public String handleAllException(Exception ex) {
        JsonObject jsonObject = new JsonObject();

        if(ex instanceof SQLGrammarException){
            jsonObject.addProperty("status", "500");
            jsonObject.addProperty("message", "SQLGrammarException");
        }

        return jsonObject.toString();

    }
}
