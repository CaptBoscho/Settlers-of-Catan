package com.dargueta.client.services;

import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * A playground area to manually test service code
 *
 * @author Derek Argueta
 */
public class ServerRunner {

    public static void main(String[] args) throws MissingUserCookieException, UnsupportedEncodingException {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", "Sam");
        obj.addProperty("password", "sam");
        obj.addProperty("playerID", 4);
        System.out.println(URLEncoder.encode(obj.toString(), "UTF-8"));

    }
}