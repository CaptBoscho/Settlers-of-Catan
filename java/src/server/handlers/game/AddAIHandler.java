package server.handlers.game;

import server.commands.CommandExecutionResult;
import server.controllers.GameController;
import server.controllers.MovesController;
import shared.dto.AddAIDTO;
import shared.dto.BuildCityDTO;
import shared.dto.CookieWrapperDTO;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author Derek Argueta
 */
public class AddAIHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        // TODO - validation


        CookieWrapperDTO dto = new CookieWrapperDTO(new AddAIDTO(request.body()));
        dto.extractCookieInfo(request.cookies());

        CommandExecutionResult result = GameController.addAI(dto);
        if(result.errorOccurred()) {
            response.status(result.getStatus());
        } else {
            response.status(200);
        }

        return result.getBody();

    }
}
