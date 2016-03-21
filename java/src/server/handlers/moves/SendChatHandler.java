package server.handlers.moves;

import server.commands.CommandExecutionResult;
import server.controllers.MovesController;
import shared.dto.CookieWrapperDTO;
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

        CookieWrapperDTO dto = new CookieWrapperDTO(new SendChatDTO(request.body()));
        dto.extractCookieInfo(request.cookies());

        CommandExecutionResult result = MovesController.sendChat(dto);
        if(result.errorOccurred()) {
            response.status(result.getStatus());
        } else {
            response.status(200);
        }

        return result.getBody();
    }
}
