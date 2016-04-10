package server.handlers.game;

import server.commands.CommandExecutionResult;
import server.controllers.GameController;
import server.persistence.provider.IPersistenceProvider;
import server.persistence.provider.PersistenceProvider;
import shared.dto.AddAIDTO;
import shared.dto.CookieWrapperDTO;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author Derek Argueta
 */
public class AddAIHandler implements Route {
    private final IPersistenceProvider persistence = PersistenceProvider.getInstance();

    @Override
    public Object handle(Request request, Response response) throws Exception {
        // TODO - validation

        CookieWrapperDTO dto = new CookieWrapperDTO(new AddAIDTO(request.body()));
        dto.extractCookieInfo(request.cookies());

        CommandExecutionResult result = GameController.addAI(dto);
        if(result.errorOccurred()) {
            response.status(result.getStatus());
        } else {
            response.status(200);

            //Save the command to the db
            persistence.getCommandDAO();//.storeCommand(dto);
        }

        return result.getBody();
    }
}
