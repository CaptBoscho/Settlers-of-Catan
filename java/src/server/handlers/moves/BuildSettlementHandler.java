package server.handlers.moves;

import server.commands.CommandExecutionResult;
import server.controllers.MovesController;
import shared.dto.BuildSettlementDTO;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author Derek Argueta
 */
public class BuildSettlementHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        // TODO - validation

        CommandExecutionResult result = MovesController.buildSettlement(new BuildSettlementDTO(request.body()));
        if(result.errorOccurred()) {
            response.status(result.getStatus());
        } else {
            response.status(200);
        }

        return result.getBody();
    }
}
