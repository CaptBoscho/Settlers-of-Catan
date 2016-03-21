package server.handlers.games;

import server.commands.CommandExecutionResult;
import server.controllers.GamesController;
import shared.dto.CreateGameDTO;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Map;

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

        // set any new cookies
        if(result.hasNewCookies()) {
            Map<String, String> cookies = result.getNewCookies();
            for(String key : cookies.keySet()) {
                response.cookie(key, cookies.get(key));
            }
        }

        return result.getBody();
    }
}
