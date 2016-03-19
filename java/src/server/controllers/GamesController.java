package server.controllers;

import com.google.gson.JsonObject;
import server.exceptions.CommandExecutionFailedException;
import server.factories.GamesCommandFactory;

/**
 * @author Derek Argueta
 */
public class GamesController {

    public static String createGame(final JsonObject body) {
        try {
            return GamesCommandFactory.getInstance().createCommand("").execute().toString();
        } catch (CommandExecutionFailedException e) {
            e.printStackTrace();
            return "return something else here.";
        }
    }

    public static String joinGame(final JsonObject body) {
        try {
            return GamesCommandFactory.getInstance().createCommand("").execute().toString();
        } catch (CommandExecutionFailedException e) {
            e.printStackTrace();
            return "return something else here.";
        }
    }

    public static String listCommand(final JsonObject body) {
        try {
            return GamesCommandFactory.getInstance().createCommand("").execute().toString();
        } catch (CommandExecutionFailedException e) {
            e.printStackTrace();
            return "return something else here.";
        }
    }
}
