package server.handlers.games;

import server.commands.CommandExecutionResult;
import server.controllers.GamesController;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author Derek Argueta
 * {@link} http://sparkjava.com/documentation.html#routes
 */
public final class ListGamesHandler implements Route {

    @Override
    public Object handle(final Request request, final Response response)  {
        final CommandExecutionResult result = GamesController.listGame();
        if(result.errorOccurred()) {
            response.status(result.getStatus());
        } else {
            response.status(200);
        }

        return result.getBody();
    }
}
