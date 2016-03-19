package server.handlers.games;

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
        return GamesController.createGame(new CreateGameDTO(request.body()));
    }
}
