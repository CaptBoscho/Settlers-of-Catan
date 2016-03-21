package server.handlers.game;

import server.commands.CommandExecutionResult;
import server.controllers.GameController;
import shared.dto.CookieWrapperDTO;
import shared.dto.GameModelDTO;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Handles HTTP GET request for fetching the game model.
 *
 * @author Derek Argueta
 */
public class ModelHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        //TODO: validation

        int requestedVersion = Integer.parseInt(request.queryMap("version").value());
        GameModelDTO gameModelDTO = new GameModelDTO(requestedVersion);
        CookieWrapperDTO dto = new CookieWrapperDTO(gameModelDTO);
        dto.extractCookieInfo(request.cookies());

        CommandExecutionResult result = GameController.getModel(dto);
        if(result.errorOccurred()) {
            response.status(result.getStatus());
        } else {
            response.status(200);
        }

        return result.getBody();
    }
}
