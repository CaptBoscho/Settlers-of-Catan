package server.handlers.moves;

import server.commands.CommandExecutionResult;
import server.controllers.MovesController;
import static server.utils.Strings.BAD_JSON_MESSAGE;

import shared.dto.CookieWrapperDTO;
import shared.dto.RollNumberDTO;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author Derek Argueta
 */
public final class RollNumberHandler implements Route {
//    private final IPersistenceProvider persistence = PersistenceProvider.getInstance();

    @Override
    public Object handle(final Request request, final Response response) throws Exception {
        if(!RollNumberDTO.isValidRequestJson(request.body())) {
            response.status(400);
            return BAD_JSON_MESSAGE;
        }

        final CookieWrapperDTO dto = new CookieWrapperDTO(new RollNumberDTO(request.body()));
        dto.extractCookieInfo(request.cookies());

        final CommandExecutionResult result = MovesController.rollNumber(dto);
        if(result.errorOccurred()) {
            response.status(result.getStatus());
        } else {
            response.status(200);

            //Save the command to the db
//            persistence.getCommandDAO();//.storeCommand(dto);
        }

        return result.getBody();
    }
}
