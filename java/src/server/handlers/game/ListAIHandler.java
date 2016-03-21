package server.handlers.game;

import server.commands.CommandExecutionResult;
import server.controllers.GameController;
import server.controllers.GamesController;
import shared.dto.ListAIDTO;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author Derek Argueta
 */
public class ListAIHandler implements Route {
     @Override
    public Object handle(Request request, Response response)  {
        final CommandExecutionResult result = GameController.listAI();
        if(result.errorOccurred()) {
            response.status(result.getStatus());
        } else {
            response.status(200);
        }

        return result.getBody();
    }
}
