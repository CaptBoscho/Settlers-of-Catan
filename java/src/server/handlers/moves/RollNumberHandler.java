package server.handlers.moves;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.commands.CommandExecutionResult;
import server.controllers.MovesController;
import shared.dto.CookieWrapperDTO;
import shared.dto.RollNumberDTO;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author Derek Argueta
 */
public class RollNumberHandler implements Route {

    // -- request keys
    private static final String kType = "type";
    private static final String kPlayerIndex = "playerIndex";
    private static final String kNumber = "number";

    @Override
    public Object handle(Request request, Response response) throws Exception {
        if(!this.validateRequest(request.body())) {
            response.status(400);
            return "Invalid request.";
        }

        // TODO - validation

        CookieWrapperDTO dto = new CookieWrapperDTO(new RollNumberDTO(request.body()));
        dto.extractCookieInfo(request.cookies());

        CommandExecutionResult result = MovesController.rollNumber(dto);
        if(result.errorOccurred()) {
            response.status(result.getStatus());
        } else {
            response.status(200);
        }

        return result.getBody();
    }

    private boolean validateRequest(String requestBody) {
        final JsonObject obj = new JsonParser().parse(requestBody).getAsJsonObject();
        final boolean hasType = obj.has(kType) && obj.get(kType).isJsonPrimitive();
        final boolean hasPlayerIndex = obj.has(kPlayerIndex) && obj.get(kPlayerIndex).isJsonPrimitive();
        final boolean hasNumber = obj.has(kNumber) && obj.get(kNumber).isJsonPrimitive();

        return hasType && hasPlayerIndex && hasNumber;
    }
}
