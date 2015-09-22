package com.sergeev.controlpanel.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sergeev.controlpanel.model.Node;
import com.sergeev.controlpanel.model.dao.NodeDaoImpl;
import com.sergeev.controlpanel.utils.Utils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;

/**
 * Created by dmitry-sergeev on 02.09.15.
 */
@Controller
public class MainController {

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(MainController.class);

    @Autowired
    private NodeDaoImpl nodeDao;

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

    @RequestMapping(value = "/admin**", method = RequestMethod.GET)
    public String adminPage(Model model){
        LOG.debug("Received /admin request...");
        model.addAttribute("responseJson", "Restricted area!");
        return "admin/admin";
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
