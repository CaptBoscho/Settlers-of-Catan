package server.handlers.games;

import server.commands.CommandExecutionResult;
import server.controllers.GamesController;
import server.controllers.MovesController;
import shared.dto.BuildCityDTO;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author Derek Argueta
 */
public class ListGamesHandler implements Route {

    @Override
    public Object handle(Request request, Response response)  {
        final CommandExecutionResult result = GamesController.listGame();
        if(result.errorOccurred()) {
            response.status(result.getStatus());
        } else {
            response.status(200);
        }

        return result.getBody();
    }
}
