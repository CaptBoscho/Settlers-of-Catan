package server.commands.game;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import shared.dto.IDTO;

/**
 * A command object that list the AI
 *
 * @author Joel Bradley
 */
public class ListAICommand implements ICommand {

    /**
     * Communicates with the ServerFacade to carry out the List AI command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() {
        return null;
    }

    @Override
    public void setParams(IDTO dto) {

    }

}
