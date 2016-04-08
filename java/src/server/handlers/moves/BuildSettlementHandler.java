package server.handlers.moves;

import server.commands.CommandExecutionResult;
import server.controllers.MovesController;
import server.persistence.provider.IPersistenceProvider;
import server.persistence.provider.PersistenceProvider;
import shared.dto.BuildSettlementDTO;
import shared.dto.CookieWrapperDTO;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author Derek Argueta
 * {@link} http://sparkjava.com/documentation.html#routes
 */
public class BuildSettlementHandler implements Route {
    private final IPersistenceProvider persistence = PersistenceProvider.getInstance();

    @Override
    public Object handle(Request request, Response response) throws Exception {
        // TODO - validation

        CookieWrapperDTO dto = new CookieWrapperDTO(new BuildSettlementDTO(request.body()));
        dto.extractCookieInfo(request.cookies());

        CommandExecutionResult result = MovesController.buildSettlement(dto);
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
