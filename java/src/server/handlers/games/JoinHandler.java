package server.handlers.games;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.commands.CommandExecutionResult;
import server.controllers.GamesController;
import shared.dto.CookieWrapperDTO;
import shared.dto.JoinGameDTO;
import spark.Request;
import spark.Response;
import spark.Route;

import java.net.URLDecoder;

/**
 * @author Derek Argueta
 *
 * Handles HTTP requests for joining a game
 */
public class JoinHandler implements Route {

    @Override
    public Object handle(Request request, Response response) throws Exception {
        if(!JoinGameDTO.isValidRequestJson(request.body())) {
            response.status(400);
            return "Invalid request";
        }

        CookieWrapperDTO dto = new CookieWrapperDTO(new JoinGameDTO(request.body()));
        dto.extractCookieInfo(request.cookies());

        CommandExecutionResult result = GamesController.joinGame(dto);
        if(result.errorOccurred()) {
            response.status(result.getStatus());
        } else {
            response.status(200);
        }

        return result.getBody();
    }
}
