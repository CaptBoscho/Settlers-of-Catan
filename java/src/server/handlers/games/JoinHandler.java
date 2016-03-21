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

        //// literally THE dumbest way to do cookies. terribad design 340 peeps
        final String decodedCookie = URLDecoder.decode(request.cookie("catan.user"), "UTF-8");
        JsonObject crappyCookieDesign = new JsonParser().parse(decodedCookie).getAsJsonObject();

        dto.setPlayerId(Integer.parseInt(crappyCookieDesign.get("playerID").getAsString()));
        dto.setUsername(crappyCookieDesign.get("name").getAsString());

        // like seriously, who stores a password as PLAIN TEXT in a cookie. cmon.

        CommandExecutionResult result = GamesController.joinGame(dto);
        if(result.errorOccurred()) {
            response.status(result.getStatus());
        } else {
            response.status(200);
        }

        return result.getBody();
    }
}
