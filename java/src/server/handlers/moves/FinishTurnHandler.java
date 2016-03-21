package server.handlers.moves;

import server.commands.CommandExecutionResult;
import server.controllers.MovesController;
import shared.dto.CookieWrapperDTO;
import shared.dto.FinishTurnDTO;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author Derek Argueta
 */
public class FinishTurnHandler implements Route {

    @Override
    public Object handle(Request request, Response response) throws Exception {
        if(!FinishTurnDTO.isValidRequestJson(request.body())) {
            response.status(400);
            return "Invalid request.";
        }

        // TODO - validation

        CookieWrapperDTO dto = new CookieWrapperDTO(new FinishTurnDTO(request.body()));
        dto.extractCookieInfo(request.cookies());

        CommandExecutionResult result = MovesController.finishTurn(dto);
        if(result.errorOccurred()) {
            response.status(result.getStatus());
        } else {
            response.status(200);
        }

        return result.getBody();
    }
}
