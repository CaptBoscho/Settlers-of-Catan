package server.handlers.games;

import server.commands.CommandExecutionResult;
import server.controllers.GamesController;
import static server.utils.Strings.BAD_JSON_MESSAGE;
import shared.dto.CookieWrapperDTO;
import shared.dto.JoinGameDTO;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Map;

/**
 * @author Derek Argueta
 *
 * Handles HTTP requests for joining a game
 * {@link} http://sparkjava.com/documentation.html#routes
 */
public class JoinHandler implements Route {

    @Override
    public Object handle(Request request, Response response) throws Exception {
        if(!JoinGameDTO.isValidRequestJson(request.body())) {
            response.status(400);
            return BAD_JSON_MESSAGE;
        }

        CookieWrapperDTO dto = new CookieWrapperDTO(new JoinGameDTO(request.body()));
        dto.extractCookieInfo(request.cookies());

        CommandExecutionResult result = GamesController.joinGame(dto);
        if(result.errorOccurred()) {
            response.status(result.getStatus());
        } else {
            response.status(200);
        }

        // set any new cookies
        if(result.hasNewCookies()) {
            Map<String, String> cookies = result.getNewCookies();
            for(String key : cookies.keySet()) {
                response.cookie(key, cookies.get(key));
            }
        }

        return result.getBody();
    }
}
