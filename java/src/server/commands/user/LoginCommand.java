package server.commands.user;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;

/**
 * A command object that logs a player in
 *
 * @author Danny Harding
 */
public class LoginCommand implements ICommand {

    /**
     * Communicates with the ServerFacade to carry out the Login command
     *
     * @return CommandExecutionResult with information about the login
     */
    @Override
    public CommandExecutionResult execute() {
        return null;
    }
}
