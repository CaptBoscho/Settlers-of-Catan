package server.handlers.moves;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.commands.CommandExecutionResult;
import server.controllers.MovesController;
import shared.dto.PlayYOPCardDTO;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Handles HTTP requests for playing the Year of Plenty card
 *
 * @author Derek Argueta
 * {@link} http://sparkjava.com/documentation.html#routes
 */
public class YearOfPlentyHandler implements Route {

    // -- request keys
    private static final String kType = "type";
    private static final String kPlayerIndex = "playerIndex";
    private static final String kResource1 = "resource1";
    private static final String kResource2 = "resource2";

    @Override
    public Object handle(Request request, Response response) throws Exception {
        if(!this.requestIsValid(request.body())) {
            response.status(400);
            return "Invalid request";
        }

        // TODO - validation

        CommandExecutionResult result = MovesController.yearOfPlenty(new PlayYOPCardDTO(request.body()));
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
        final boolean hasResource1 = obj.has(kResource1) && obj.get(kResource1).isJsonPrimitive();
        final boolean hasResource2 = obj.has(kResource2) && obj.get(kResource2).isJsonPrimitive();

        return hasType && hasPlayerIndex && hasResource1 && hasResource2;
    }
}
