package server.handlers.game;

import server.commands.CommandExecutionResult;
import server.controllers.GameController;
import server.controllers.GamesController;
import shared.dto.CookieWrapperDTO;
import shared.dto.ListAIDTO;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author Derek Argueta
 * {@link} http://sparkjava.com/documentation.html#routes
 */
public final class ListAIHandler implements Route {
    @Override
    public Object handle(final Request request, final Response response) {
        CookieWrapperDTO dto = new CookieWrapperDTO(new ListAIDTO(request.body()));
        dto.extractCookieInfo(request.cookies());

        final CommandExecutionResult result = GameController.listAI(dto);
        if(result.errorOccurred()) {
            response.status(result.getStatus());
        } else {
            response.status(200);
        }

        return result.getBody();
    }
}
