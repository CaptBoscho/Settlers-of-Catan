package server.handlers.game;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.controllers.GamesController;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author Derek Argueta
 */
public class ListAIHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        response.status(200);
        response.type("application/json");
        final JsonObject body = new JsonParser().parse(request.body()).getAsJsonObject();
        return GamesController.listCommand(body);
    }
}
