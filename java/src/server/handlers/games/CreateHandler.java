package server.handlers.games;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.controllers.GamesController;
import shared.dto.CreateGameDTO;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author Derek Argueta
 */
public class CreateHandler implements Route {

    @Override
    public Object handle(final Request request, final Response response) throws Exception {
        if(!CreateGameDTO.isValidRequestJson(request.body())) {
            response.status(400);
            return "Invalid request";
        }

        response.status(200);
        response.type("application/json");
        final JsonObject body = new JsonParser().parse(request.body()).getAsJsonObject();
        return GamesController.createGame(body);
    }
}
