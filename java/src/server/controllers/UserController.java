package server.controllers;

import server.commands.CommandExecutionResult;
import server.factories.UserCommandFactory;
import shared.dto.AuthDTO;

/**
 * @author Derek Argueta
 */
public class UserController {

    public static CommandExecutionResult login(final AuthDTO dto) {
        try {
            return UserCommandFactory.getInstance().executeCommand("login", dto);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static CommandExecutionResult register(final AuthDTO dto) {
        try {
            return UserCommandFactory.getInstance().executeCommand("register", dto);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
