package server.handlers.auth;

import server.commands.CommandExecutionResult;
import server.controllers.UserController;
import static server.utils.Strings.BAD_JSON_MESSAGE;
import shared.dto.AuthDTO;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.servlet.http.Cookie;
import java.util.Map;

/**
 * @author Derek Argueta
 * {@link} http://sparkjava.com/documentation.html#routes
 */
public class RegisterHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        if(!AuthDTO.isValidRequestJson(request.body())) {
            response.status(400);
            return BAD_JSON_MESSAGE;
        }

        CommandExecutionResult result = UserController.register(new AuthDTO(request.body()));
        if(result.errorOccurred()) {
            response.status(result.getStatus());
        } else {
            response.status(200);
        }

        // set any new cookies
        if(result.hasNewCookies()) {
            Map<String, String> cookies = result.getNewCookies();
            for(String key : cookies.keySet()) {
                Cookie cookie = new Cookie(key, cookies.get(key));
                cookie.setPath("/");
                response.raw().addCookie(cookie);
            }
        }

        return result.getBody();
    }
}
