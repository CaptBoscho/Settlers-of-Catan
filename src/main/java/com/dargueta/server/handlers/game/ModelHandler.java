package server.handlers.game;

import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author Derek Argueta
 */
public class ModelHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        response.status(200);
        response.type("application/json");
        return "{\"Hello\": \"World!\"}";
    }
}
