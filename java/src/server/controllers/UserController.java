package server.controllers;

import server.commands.CommandExecutionResult;
import server.commands.CommandName;
import server.factories.UserCommandFactory;
import server.persistence.PersistenceCoordinator;
import server.persistence.dto.UserDTO;
import shared.dto.AuthDTO;
import shared.dto.IDTO;

import static server.commands.CommandName.*;

/**
 * @author Derek Argueta
 */
public final class UserController {

    public static CommandExecutionResult login(final AuthDTO dto) {
        return executeCommand(USER_LOGIN, dto);
    }

    public static CommandExecutionResult register(final AuthDTO dto) {
        CommandExecutionResult result = executeCommand(USER_REGISTER, dto);
        UserDTO userDto = new UserDTO(Integer.parseInt(result.getNewCookies().get("totalhack")), dto.getUsername(), dto.getPassword());
        PersistenceCoordinator.addUser(userDto);
        return result;
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
