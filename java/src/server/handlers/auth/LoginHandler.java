package server.handlers.auth;

import server.controllers.UserController;
import shared.dto.AuthDTO;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author Derek Argueta
 */
public class LoginHandler implements Route {

    @Override
    public Object handle(Request request, Response response) throws Exception {
        if(!AuthDTO.isValidRequestJson(request.body())) {
            response.status(400);
            return "Invalid request.";
        }

        response.status(200);
        response.type("application/json");
        return UserController.login(new AuthDTO(request.body()));
    }
}
