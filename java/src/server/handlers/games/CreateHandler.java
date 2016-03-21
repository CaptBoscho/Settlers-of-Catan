package server.handlers.games;

import server.commands.CommandExecutionResult;
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
            return "Invalid request.";
        }

        CommandExecutionResult result = GamesController.createGame(new CreateGameDTO(request.body()));

        if(result.errorOccurred()) {
            response.status(result.getStatus());
        } else {
            response.status(200);
        }

        return result.getBody();
    }
}
