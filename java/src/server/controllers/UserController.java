package server.controllers;

import com.google.gson.JsonObject;
import server.exceptions.CommandExecutionFailedException;
import server.factories.UserCommandFactory;

/**
 * @author Derek Argueta
 */
public class UserController {

    public static String login(final JsonObject body) {
        try {
            return UserCommandFactory.getInstance().createCommand("").execute().toString();
        } catch (CommandExecutionFailedException e) {
            e.printStackTrace();
            return "return something else here.";
        }
    }

    public static String register(final JsonObject body) {
        try {
            return UserCommandFactory.getInstance().createCommand("").execute().toString();
        } catch (CommandExecutionFailedException e) {
            e.printStackTrace();
            return "return something else here.";
        }
    }
}
