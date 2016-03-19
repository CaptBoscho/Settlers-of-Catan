package server.controllers;

import com.google.gson.JsonObject;
import server.exceptions.CommandExecutionFailedException;
import server.factories.UserCommandFactory;
import shared.dto.AuthDTO;

/**
 * @author Derek Argueta
 */
public class UserController {

    public static JsonObject login(final AuthDTO dto) {
        try {
            return UserCommandFactory.getInstance().createCommand(dto).execute().toJSON();
        } catch (CommandExecutionFailedException e) {
            e.printStackTrace();
            //TODO: fix this fool
            return new JsonObject();
        }
    }

    public static JsonObject register(final AuthDTO dto) {
        try {
            return UserCommandFactory.getInstance().createCommand(dto).execute().toJSON();
        } catch (CommandExecutionFailedException e) {
            e.printStackTrace();
            //TODO: fix this fool
            return new JsonObject();
        }
    }
}
