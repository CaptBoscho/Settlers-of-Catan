package server.controllers;

import com.google.gson.JsonObject;
import server.factories.GamesCommandFactory;

/**
 * @author Derek Argueta
 */
public class GamesController {

    public static String createGame(final JsonObject body) {
        return GamesCommandFactory.getInstance().createCommand("").execute().toString();
    }

    public static String joinGame(final JsonObject body) {
        return GamesCommandFactory.getInstance().createCommand("").execute().toString();
    }

    public static String listCommand(final JsonObject body) {
        return GamesCommandFactory.getInstance().createCommand("").execute().toString();
    }
}
