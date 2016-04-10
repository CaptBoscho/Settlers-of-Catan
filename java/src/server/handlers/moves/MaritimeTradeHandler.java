package server.handlers.moves;

import server.commands.CommandExecutionResult;
import server.controllers.MovesController;
import static server.utils.Strings.BAD_JSON_MESSAGE;

import shared.dto.CookieWrapperDTO;
import shared.dto.MaritimeTradeDTO;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author Derek Argueta
 * {@link} http://sparkjava.com/documentation.html#routes
 */
public class MaritimeTradeHandler implements Route {

    @Override
    public Object handle(Request request, Response response) throws Exception {
        if(!MaritimeTradeDTO.isValidRequestJson(request.body())) {
            response.status(400);
            return BAD_JSON_MESSAGE;
        }

        CookieWrapperDTO dto = new CookieWrapperDTO(new MaritimeTradeDTO(request.body()));
        dto.extractCookieInfo(request.cookies());

        CommandExecutionResult result = MovesController.maritimeTrade(dto);
        if(result.errorOccurred()) {
            response.status(result.getStatus());
        } else {
            response.status(200);
        }

        return result.getBody();
    }
}
