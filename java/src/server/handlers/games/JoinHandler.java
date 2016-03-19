package server.handlers.games;

import server.controllers.GamesController;
import shared.dto.JoinGameDTO;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author Derek Argueta
 */
public class JoinHandler implements Route {

    @Override
    public Object handle(Request request, Response response) throws Exception {
        if(!JoinGameDTO.isValidRequestJson(request.body())) {
            response.status(400);
            return "Invalid request";
        }

        response.status(200);
        response.type("application/json");
        return GamesController.joinGame(new JoinGameDTO(request.body()));
    }
}
