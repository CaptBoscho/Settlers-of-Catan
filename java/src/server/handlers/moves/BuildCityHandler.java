package server.handlers.moves;

import server.commands.CommandExecutionResult;
import server.controllers.MovesController;
import shared.dto.BuildCityDTO;
import shared.dto.CookieWrapperDTO;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author Derek Argueta
 * {@link} http://sparkjava.com/documentation.html#routes
 */
public final class BuildCityHandler implements Route {
    @Override
    public Object handle(final Request request, final Response response) throws Exception {
        // TODO - validation


        CookieWrapperDTO dto = new CookieWrapperDTO(new BuildCityDTO(request.body()));
        dto.extractCookieInfo(request.cookies());

        CommandExecutionResult result = MovesController.buildCity(dto);
        if(result.errorOccurred()) {
            response.status(result.getStatus());
        } else {
            response.status(200);
        }

        return result.getBody();

    }
}
