package server.handlers.moves;

import server.commands.CommandExecutionResult;
import server.controllers.MovesController;
import shared.dto.CookieWrapperDTO;
import shared.dto.PlaySoldierCardDTO;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author Derek Argueta
 */
public final class SoldierHandler implements Route {
    @Override
    public Object handle(final Request request, final Response response) throws Exception {
        // TODO - validation

        final CookieWrapperDTO dto = new CookieWrapperDTO(new PlaySoldierCardDTO(request.body()));
        dto.extractCookieInfo(request.cookies());

        final CommandExecutionResult result = MovesController.soldier(dto);
        if(result.errorOccurred()) {
            response.status(result.getStatus());
        } else {
            response.status(200);
        }

        return result.getBody();
    }
}
