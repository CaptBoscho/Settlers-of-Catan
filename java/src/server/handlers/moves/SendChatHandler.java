package server.handlers.moves;

import server.commands.CommandExecutionResult;
import server.controllers.MovesController;
import static server.utils.Strings.BAD_JSON_MESSAGE;

import server.persistence.provider.IPersistenceProvider;
import server.persistence.provider.PersistenceProvider;
import shared.dto.CookieWrapperDTO;
import shared.dto.SendChatDTO;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author Derek Argueta
 */
public class SendChatHandler implements Route {
    private final IPersistenceProvider persistence = PersistenceProvider.getInstance();

    @Override
    public Object handle(Request request, Response response) throws Exception {
        if(!SendChatDTO.isValidRequestJson(request.body())) {
            response.status(400);
            return BAD_JSON_MESSAGE;
        }

        final CookieWrapperDTO dto = new CookieWrapperDTO(new SendChatDTO(request.body()));
        dto.extractCookieInfo(request.cookies());

        final CommandExecutionResult result = MovesController.sendChat(dto);
        if(result.errorOccurred()) {
            response.status(result.getStatus());
        } else {
            response.status(200);

            //Save the command to the db
            persistence.startTransaction();
            persistence.getCommandDAO();//.storeCommand(dto);
            persistence.endTransaction(true);
        }

        return result.getBody();
    }
}
