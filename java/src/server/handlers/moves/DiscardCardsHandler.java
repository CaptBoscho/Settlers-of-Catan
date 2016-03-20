package server.handlers.moves;

import server.commands.CommandExecutionResult;
import server.controllers.MovesController;
import shared.dto.DiscardCardsDTO;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author Derek Argueta
 */
public class DiscardCardsHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        // TODO - validation

        CommandExecutionResult result = MovesController.discardCards(new DiscardCardsDTO(request.body()));
        if(result.errorOccurred()) {
            response.status(result.getStatus());
        } else {
            response.status(200);
        }

        return result.getBody();
    }
}
