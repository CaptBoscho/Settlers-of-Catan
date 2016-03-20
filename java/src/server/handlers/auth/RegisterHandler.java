package server.handlers.auth;

import server.commands.CommandExecutionResult;
import server.controllers.UserController;
import shared.dto.AuthDTO;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Derek Argueta
 */
public class RegisterHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        if(!AuthDTO.isValidRequestJson(request.body())) {
            response.status(400);
            return "Invalid request.";
        }

        response.status(200);

        // set any new cookies
        CommandExecutionResult result = UserController.register(new AuthDTO(request.body()));
        if(result.hasNewCookies()) {
            Map<String, String> cookies = result.getNewCookies();
            for(String key : cookies.keySet()) {
                response.cookie(key, cookies.get(key));
            }
        }

        return result.getBody();
    }
}
