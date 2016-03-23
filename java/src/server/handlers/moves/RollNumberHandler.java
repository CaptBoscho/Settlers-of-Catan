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
public class RollNumberHandler implements Route {

    @Override
    public Object handle(Request request, Response response) throws Exception {
        if(!RollNumberDTO.isValidRequestJson(request.body())) {
            response.status(400);
            return BAD_JSON_MESSAGE;
        }

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
}
