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
public class BuyDevCardHandler implements Route {

    // -- TODO add request validation

    @Override
    public Object handle(Request request, Response response) throws Exception {
        response.status(200);
        response.type("application/json");
        final JsonObject body = new JsonParser().parse(request.body()).getAsJsonObject();
        return MovesController.buyDevCard(body).toString();
    }
}
