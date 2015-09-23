package com.sergeev.controlpanel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by dmitry-sergeev on 23.09.15.
 */
@Controller
public class ErrorController {
    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public String page403(){
        return "errors/403";
    }
    @RequestMapping(value = "/404", method = RequestMethod.GET)
    public String page404(){
        return "errors/404";
    }
    @RequestMapping(value = "/500", method = RequestMethod.GET)
    public String page500(){
        return "errors/500";
    }
}
