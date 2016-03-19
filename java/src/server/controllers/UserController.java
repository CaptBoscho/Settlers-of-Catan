package server.controllers;

import com.google.gson.JsonObject;
import server.exceptions.CommandExecutionFailedException;
import server.factories.UserCommandFactory;
import shared.dto.AuthDTO;

/**
 * @author Derek Argueta
 */
public class UserController {

    public static String login(final AuthDTO dto) {
        try {
            return UserCommandFactory.getInstance().createCommand(dto).execute().toString();
        } catch (CommandExecutionFailedException e) {
            e.printStackTrace();
            return "return something else here.";
        }
    }

    public static String register(final AuthDTO dto) {
        try {
            return UserCommandFactory.getInstance().createCommand(dto).execute().toString();
        } catch (CommandExecutionFailedException e) {
            e.printStackTrace();
            return "return something else here.";
        }
    }
}
