package server.handlers.moves;

import server.commands.CommandExecutionResult;
import server.controllers.MovesController;
import static server.utils.Strings.BAD_JSON_MESSAGE;

import server.persistence.provider.IPersistenceProvider;
import server.persistence.provider.PersistenceProvider;
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
public final class AcceptTradeHandler implements Route {
    private final IPersistenceProvider persistence = PersistenceProvider.getInstance();

    @Override
    public Object handle(final Request request, final Response response) throws Exception {
        if(!TradeOfferResponseDTO.isValidRequestJson(request.body())) {
            response.status(400);
            return BAD_JSON_MESSAGE;
        }

        final CookieWrapperDTO dto = new CookieWrapperDTO(new TradeOfferResponseDTO(request.body()));
        dto.extractCookieInfo(request.cookies());

        final CommandExecutionResult result = MovesController.acceptTrade(dto);
        if(result.errorOccurred()) {
            response.status(result.getStatus());
        } else {
            response.status(200);

            //Save the command to the db
            persistence.getCommandDAO();//.storeCommand(dto);
        }

        return result.getBody();
    }
}
