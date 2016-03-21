package server.handlers.moves;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.commands.CommandExecutionResult;
import server.controllers.MovesController;
import shared.dto.CookieWrapperDTO;
import shared.dto.PlayMonumentDTO;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author Derek Argueta
 */
public class MonumentHandler implements Route {

    @Override
    public Object handle(Request request, Response response) throws Exception {
        if(!PlayMonumentDTO.isValidRequestJson(request.body())) {
            response.status(400);
            return "Invalid request.";
        }

        CookieWrapperDTO dto = new CookieWrapperDTO(new PlayMonumentDTO(request.body()));
        dto.extractCookieInfo(request.cookies());

        CommandExecutionResult result = MovesController.monument(dto);
        if(result.errorOccurred()) {
            response.status(result.getStatus());
        } else {
            response.status(200);
        }

        return result.getBody();
    }
}
