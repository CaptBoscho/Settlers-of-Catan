package server.handlers.moves;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.controllers.MovesController;
import shared.dto.SendChatDTO;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author Derek Argueta
 */
public class SendChatHandler implements Route {

    @Override
    public Object handle(Request request, Response response) throws Exception {
        if(!SendChatDTO.isValidRequestJson(request.body())) {
            response.status(400);
            return "Invalid request.";
        }

        response.status(200);
        response.type("application/json");
        final JsonObject body = new JsonParser().parse(request.body()).getAsJsonObject();
        return MovesController.sendChat(body).toString();
    }
}
