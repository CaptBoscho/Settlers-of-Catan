package server.handlers.moves;

import server.commands.CommandExecutionResult;
import server.controllers.MovesController;
import shared.dto.BuyDevCardDTO;
import shared.dto.CookieWrapperDTO;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author Derek Argueta
 * {@link} http://sparkjava.com/documentation.html#routes
 */
public class BuyDevCardHandler implements Route {

    @Override
    public Object handle(Request request, Response response) throws Exception {
        // TODO - validation

        CookieWrapperDTO dto = new CookieWrapperDTO(new BuyDevCardDTO(request.body()));
        dto.extractCookieInfo(request.cookies());

        CommandExecutionResult result = MovesController.buyDevCard(dto);
        if(result.errorOccurred()) {
            response.status(result.getStatus());
        } else {
            response.status(200);
        }

        return result.getBody();
    }
}
