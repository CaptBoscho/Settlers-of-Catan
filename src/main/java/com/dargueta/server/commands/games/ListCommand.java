package server.commands.games;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.main.Config;
import shared.dto.IDTO;

/**
 * A command object that lists the available games.
 *
 * @author Danny Harding
 */
public class ListCommand implements ICommand {

    /**
     * Communicates with the ServerFacade to carry out the List command
     *
     * @return CommandExecutionResult with information about the games list
     */
    @Override
    public CommandExecutionResult execute() {
        return Config.facade.list();
    }

    @Override
    public void setParams(IDTO dto) {
        // -- not needed
    }
}
