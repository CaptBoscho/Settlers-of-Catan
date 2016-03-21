package server.handlers.game;

import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author Derek Argueta
 */
public class ListAIHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        //TODO: had to comment this out to make it compile. ListAIDTO doesn't exist yet.
        //response.status(200);
        //response.type("application/json");
        //return GamesController.listCommand(new ListAIDTO(request.body()));
        return null;
    }
}
