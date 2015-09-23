package com.sergeev.controlpanel.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sergeev.controlpanel.model.AbstractModel;

import java.util.*;

/**
 * Created by dmitry-sergeev on 22.09.15.
 */
public class Utils {

    public static String constructJsonAnswer(AbstractModel entity){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", "ok");
        jsonObject.add("entity", new JsonParser().parse(entity.toString())); //TODO redundant parse
        return jsonObject.toString();
    }

    public static String constructJsonAnswer(List<? extends AbstractModel> entities){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", "ok");
        jsonObject.add("entities", new Gson().toJsonTree(entities));
        return jsonObject.toString();
    }


}
