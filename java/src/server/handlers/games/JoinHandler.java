package server.handlers.games;

import server.commands.CommandExecutionResult;
import server.controllers.GamesController;

import static server.utils.Strings.BAD_JSON_MESSAGE;

import server.persistence.provider.IPersistenceProvider;
import server.persistence.provider.PersistenceProvider;
import shared.dto.CookieWrapperDTO;
import shared.dto.JoinGameDTO;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.servlet.http.Cookie;
import java.util.Map;

/**
 * @author Derek Argueta
 *
 * Handles HTTP requests for joining a game
 * {@link} http://sparkjava.com/documentation.html#routes
 */
public final class JoinHandler implements Route {
    private final IPersistenceProvider persistence = PersistenceProvider.getInstance();

    @Override
    public Object handle(final Request request, final Response response) throws Exception {
        if(!JoinGameDTO.isValidRequestJson(request.body())) {
            response.status(400);
            return BAD_JSON_MESSAGE;
        }

        final CookieWrapperDTO dto = new CookieWrapperDTO(new JoinGameDTO(request.body()));
        dto.extractCookieInfo(request.cookies());

        final CommandExecutionResult result = GamesController.joinGame(dto);
        if(result.errorOccurred()) {
            response.status(result.getStatus());
        } else {
            response.status(200);
        }

        // set any new cookies
        if(result.hasNewCookies()) {
            final Map<String, String> cookies = result.getNewCookies();
            for(String key : cookies.keySet()) {
                Cookie cookie = new Cookie(key, cookies.get(key));
                cookie.setPath("/");
                response.raw().addCookie(cookie);
            }
        }

        return result.getBody();
    }
}
