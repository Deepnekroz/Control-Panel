package com.sergeev.controlpanel.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sergeev.controlpanel.controller.MainController;
import com.sergeev.controlpanel.model.AbstractModel;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

/**
 * Created by dmitry-sergeev on 22.09.15.
 */
public class Utils {

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(Utils.class);

    public static ResponseEntity constructJsonAnswer(AbstractModel entity){
        if(entity==null)
            return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
        JsonObject jsonObject = new JsonObject();
        int status = 200;
        jsonObject.addProperty("status", status);
        jsonObject.add("entity", new JsonParser().parse(entity.toString())); //TODO redundant parse
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.valueOf(status));
    }

    public static ResponseEntity constructJsonAnswer(Set<? extends AbstractModel> entities){
        if(entities==null)
            return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
        JsonObject jsonObject = new JsonObject();
        int status = 200;
        jsonObject.addProperty("status", status);
        jsonObject.add("entities", new Gson().toJsonTree(entities));
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.valueOf(status));
    }

    public static ResponseEntity status(int status){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", HttpStatus.valueOf(status).name());
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.valueOf(status));
    }

}
