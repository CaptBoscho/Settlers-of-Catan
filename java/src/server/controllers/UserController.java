package server.controllers;

import com.google.gson.JsonObject;
import server.factories.UserCommandFactory;

/**
 * @author Derek Argueta
 */
public class UserController {

    public static String login(final JsonObject body) {
        return UserCommandFactory.getInstance().createCommand("").execute().toString();
    }

    public static String register(final JsonObject body) {
        return UserCommandFactory.getInstance().createCommand("").execute().toString();
    }
}
