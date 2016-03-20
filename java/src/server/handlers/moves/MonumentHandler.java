package server.handlers.moves;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.commands.CommandExecutionResult;
import server.controllers.MovesController;
import shared.dto.PlayMonumentDTO;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author Derek Argueta
 */
public class MonumentHandler implements Route {

    // -- request keys
    private static final String kType = "type";
    private static final String kPlayerIndex = "playerIndex";

    @Override
    public Object handle(Request request, Response response) throws Exception {
        if(!this.requestIsValid(request.body())) {
            response.status(400);
            return "Invalid request.";
        }

        // TODO - validation

        CommandExecutionResult result = MovesController.monument(new PlayMonumentDTO(request.body()));
        if(result.errorOccurred()) {
            response.status(result.getStatus());
        } else {
            response.status(200);
        }

        return result.getBody();
    }

    private boolean requestIsValid(final String requestBody) {
        final JsonObject obj = new JsonParser().parse(requestBody).getAsJsonObject();
        final boolean hasType = obj.has(kType) && obj.get(kType).isJsonPrimitive();
        final boolean hasPlayerIndex = obj.has(kPlayerIndex) && obj.get(kPlayerIndex).isJsonPrimitive();

        return hasType && hasPlayerIndex;
    }
}
