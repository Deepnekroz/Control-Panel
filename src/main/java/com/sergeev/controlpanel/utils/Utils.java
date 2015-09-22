package com.sergeev.controlpanel.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;

/**
 * Created by dmitry-sergeev on 22.09.15.
 */
public class Utils {
    public static String constructJsonAnswer(Object entity){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", "ok");
        jsonObject.add("node", new JsonParser().parse(entity.toString())); //TODO redundant parse
        return jsonObject.toString();
    }

    public static String constructJsonAnswer(List<Object> entities){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", "ok");
        jsonObject.add("node", new Gson().toJsonTree(entities)); //TODO redundant parse
        return jsonObject.toString();
    }
}
