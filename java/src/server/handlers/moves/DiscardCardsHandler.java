package server.handlers.moves;

import server.commands.CommandExecutionResult;
import server.controllers.MovesController;
import shared.dto.CookieWrapperDTO;
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
        if(!DiscardCardsDTO.isValidRequestJson(request.body())) {
            response.status(400);
            return "Invalid request.";
        }

        CookieWrapperDTO dto = new CookieWrapperDTO(new DiscardCardsDTO(request.body()));
        dto.extractCookieInfo(request.cookies());

        CommandExecutionResult result = MovesController.discardCards(dto);
        if(result.errorOccurred()) {
            response.status(result.getStatus());
        } else {
            response.status(200);
        }

        return result.getBody();
    }
}
