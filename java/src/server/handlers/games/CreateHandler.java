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
public class CreateHandler implements Route {

    // -- request keys
    private static final String kRandomTiles = "randomTiles";
    private static final String kRandomNumbers = "randomNumbers";
    private static final String kRandomPorts = "randomPorts";
    private static final String kName = "name";

    @Override
    public Object handle(final Request request, final Response response) throws Exception {
        if(!this.validRequest(request.body())) {
            response.status(400);
            return "Invalid request";
        }

        response.status(200);
        response.type("application/json");
        final JsonObject body = new JsonParser().parse(request.body()).getAsJsonObject();
        return GamesController.createGame(body);
    }

    private boolean validRequest(final String requestBody) {
        final JsonObject obj = new JsonParser().parse(requestBody).getAsJsonObject();
        final boolean hasRandomTiles = obj.has(kRandomTiles) && obj.get(kRandomTiles).isJsonPrimitive();
        final boolean hasRandomNumbers = obj.has(kRandomNumbers) && obj.get(kRandomNumbers).isJsonPrimitive();
        final boolean hasRandomPorts = obj.has(kRandomPorts) && obj.get(kRandomPorts).isJsonPrimitive();
        final boolean hasName = obj.has(kName) && obj.get(kName).isJsonPrimitive();

        return hasRandomTiles && hasRandomNumbers && hasRandomPorts && hasName;
    }
}
