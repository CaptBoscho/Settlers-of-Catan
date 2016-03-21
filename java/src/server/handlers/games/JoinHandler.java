package server.handlers.games;

import server.commands.CommandExecutionResult;
import server.controllers.GamesController;
import shared.dto.JoinGameDTO;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author Derek Argueta
 *
 * Handles HTTP requests for joining a game
 */
public class JoinHandler implements Route {

    @Override
    public Object handle(Request request, Response response) throws Exception {
        if(!JoinGameDTO.isValidRequestJson(request.body())) {
            response.status(400);
            return "Invalid request";
        }

        CommandExecutionResult result = GamesController.joinGame(new JoinGameDTO(request.body()));
        if(result.errorOccurred()) {
            response.status(result.getStatus());
        } else {
            response.status(200);
        }

        return result.getBody();
    }
}
