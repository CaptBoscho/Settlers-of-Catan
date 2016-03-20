package server.handlers.moves;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.commands.CommandExecutionResult;
import server.controllers.MovesController;
import shared.dto.PlaySoldierCardDTO;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author Derek Argueta
 */
public class SoldierHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        // TODO - validation

        CommandExecutionResult result = MovesController.soldier(new PlaySoldierCardDTO(request.body()));
        if(result.errorOccurred()) {
            response.status(result.getStatus());
        } else {
            response.status(200);
        }

        return result.getBody();
    }
}
