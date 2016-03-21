package server.handlers.game;

import server.controllers.GameController;
import shared.dto.ListAIDTO;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author Derek Argueta
 */
public class ListAIHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        response.status(200);
        response.type("application/json");
        return GameController.listAI();
    }
}
