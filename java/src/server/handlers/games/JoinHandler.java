package server.handlers.games;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.controllers.GamesController;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author Derek Argueta
 */
public class JoinHandler implements Route {

    // -- request keys
    private static final String kId = "id";
    private static final String kColor = "color";

    @Override
    public Object handle(Request request, Response response) throws Exception {
        if(!this.validateRequest(request.body())) {
            response.status(400);
            return "Invalid request";
        }

        response.status(200);
        response.type("application/json");
        final JsonObject body = new JsonParser().parse(request.body()).getAsJsonObject();
        return GamesController.joinGame(body);
    }

    private boolean validateRequest(String requestBody) {
        final JsonObject obj = new JsonParser().parse(requestBody).getAsJsonObject();
        final boolean hasId = obj.has(kId) && obj.get(kId).isJsonPrimitive();
        final boolean hasColor = obj.has(kColor) && obj.get(kColor).isJsonPrimitive();

        return hasId && hasColor;
    }
}
