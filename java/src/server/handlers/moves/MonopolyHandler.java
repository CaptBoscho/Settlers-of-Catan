package server.handlers.moves;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.controllers.MovesController;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author Derek Argueta
 */
public class MonopolyHandler implements Route {

    // -- request keys
    private static final String kType = "type";
    private static final String kResource = "resource";
    private static final String kPlayerIndex = "playerIndex";

    @Override
    public Object handle(Request request, Response response) throws Exception {
        if(!this.requestIsValid(request.body())) {
            response.status(400);
            return "Invalid request";
        }

        response.status(200);
        response.type("application/json");
        final JsonObject body = new JsonParser().parse(request.body()).getAsJsonObject();
        return MovesController.monopoly(body).toString();
    }

    private boolean requestIsValid(final String requestBody) {
        final JsonObject obj = new JsonParser().parse(requestBody).getAsJsonObject();
        final boolean hasType = obj.has(kType) && obj.get(kType).isJsonPrimitive();
        final boolean hasResource = obj.has(kResource) && obj.get(kResource).isJsonPrimitive();
        final boolean hasPlayerIndex = obj.has(kPlayerIndex) && obj.get(kPlayerIndex).isJsonPrimitive();

        return hasType && hasResource && hasPlayerIndex;
    }
}
