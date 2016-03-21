package server.handlers.moves;

import server.commands.CommandExecutionResult;
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

        // TODO - validation

        CommandExecutionResult result = MovesController.sendChat(new SendChatDTO(request.body()));
        if(result.errorOccurred()) {
            response.status(result.getStatus());
        } else {
            response.status(200);
        }

        return result.getBody();
    }
}
