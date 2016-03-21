package server.handlers.moves;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.commands.CommandExecutionResult;
import server.controllers.MovesController;
import shared.dto.RobPlayerDTO;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author Derek Argueta
 */
public class RobPlayerHandler implements Route {

    // -- request keys
    private static final String kType = "type";
    private static final String kPlayerIndex = "playerIndex";
    private static final String kVictimIndex = "victimindex";
    private static final String kLocation = "location";
    private static final String kX = "x";
    private static final String kY = "y";

    @Override
    public Object handle(Request request, Response response) throws Exception {
        if(!this.requestIsValid(request.body())) {
            response.status(400);
            return "Invalid request.";
        }

        // TODO - validation

        CommandExecutionResult result = MovesController.robPlayer(new RobPlayerDTO(request.body()));
        if(result.errorOccurred()) {
            response.status(result.getStatus());
        } else {
            response.status(200);
        }

        return result.getBody();
    }

    private boolean requestIsValid(String requestBody) {
        final JsonObject obj = new JsonParser().parse(requestBody).getAsJsonObject();
        final boolean hasType = obj.has(kType) && obj.get(kType).isJsonPrimitive();
        final boolean hasPlayerIndex = obj.has(kPlayerIndex) && obj.get(kPlayerIndex).isJsonPrimitive();
        final boolean hasVictimIndex = obj.has(kVictimIndex) && obj.get(kVictimIndex).isJsonPrimitive();
        final boolean hasLocation = obj.has(kLocation) && obj.get(kLocation).isJsonObject();

        if(!hasLocation) return false;  // avoid crash when querying location
        final JsonObject loc = obj.get(kLocation).getAsJsonObject();
        final boolean hasX = loc.has(kX) && loc.get(kX).isJsonPrimitive();
        final boolean hasY = loc.has(kY) && loc.get(kY).isJsonPrimitive();

        return hasType && hasPlayerIndex && hasVictimIndex && hasX && hasY;
    }
}
