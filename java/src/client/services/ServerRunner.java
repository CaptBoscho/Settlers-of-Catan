package client.services;

import com.google.gson.JsonObject;
import com.sun.deploy.net.URLEncoder;
import shared.definitions.CatanColor;
import shared.dto.*;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;

import java.io.UnsupportedEncodingException;

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