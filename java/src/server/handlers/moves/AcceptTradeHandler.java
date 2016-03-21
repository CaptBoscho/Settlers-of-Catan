package server.handlers.moves;

import server.commands.CommandExecutionResult;
import server.controllers.MovesController;
import shared.dto.CookieWrapperDTO;
import shared.dto.TradeOfferResponseDTO;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Handles HTTP requests for accepting a trade offer from another player
 *
 * @author Derek Argueta
 * {@link} http://sparkjava.com/documentation.html#routes
 */
public class AcceptTradeHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
//        if(!TradeOfferResponseDTO.is)
        // TODO - validation


        CookieWrapperDTO dto = new CookieWrapperDTO(new TradeOfferResponseDTO(request.body()));
        dto.extractCookieInfo(request.cookies());

        CommandExecutionResult result = MovesController.acceptTrade(dto);
        if(result.errorOccurred()) {
            response.status(result.getStatus());
        } else {
            response.status(200);
        }

        return result.getBody();
    }
}
