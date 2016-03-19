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

        response.status(200);
        response.type("application/json");
        final JsonObject body = new JsonParser().parse(request.body()).getAsJsonObject();
        return MovesController.yearOfPlenty(body).toString();
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
