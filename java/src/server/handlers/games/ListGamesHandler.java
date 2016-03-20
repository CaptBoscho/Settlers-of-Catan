package server.handlers.games;

import server.controllers.GamesController;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author Derek Argueta
 */
public class ListGamesHandler implements Route {

    @Override
    public Object handle(Request request, Response response)  {
        try {
            return GamesController.listCommand();
        } catch (Exception e) {
            e.printStackTrace();
            response.status(500);
            return "";
        }
    }
}
