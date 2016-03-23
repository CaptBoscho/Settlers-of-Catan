package server.controllers;

import server.commands.CommandExecutionResult;
import server.commands.CommandName;
import server.factories.UserCommandFactory;
import shared.dto.AuthDTO;
import shared.dto.IDTO;

import static server.commands.CommandName.*;

/**
 * @author Derek Argueta
 */
public class UserController {

    public static CommandExecutionResult login(final AuthDTO dto) {
        return executeCommand(USER_LOGIN, dto);
    }

    public static CommandExecutionResult register(final AuthDTO dto) {
        return executeCommand(USER_REGISTER, dto);
    }

    private static CommandExecutionResult executeCommand(final CommandName commandName, final IDTO dto) {
        try {
            return UserCommandFactory.getInstance().executeCommand(commandName, dto);
        } catch (Exception e) {
            e.printStackTrace();
            CommandExecutionResult result = new CommandExecutionResult("Something went wrong :(");
            result.triggerError(500);
            return result;
        }
    }
}
