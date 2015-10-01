package com.sergeev.controlpanel.model;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Created by dmitry-sergeev on 23.09.15.
 */
public abstract class AbstractModel {
    public abstract JSONObject toJson();
}
