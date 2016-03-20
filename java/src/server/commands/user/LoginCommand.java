package server.commands.user;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import shared.dto.IDTO;

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

    @Override
    public void setParams(IDTO dto) {

    }
}
