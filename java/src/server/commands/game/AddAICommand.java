package server.commands.game;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import shared.dto.AddAIDTO;
import shared.dto.IDTO;

/**
 * A command object that adds an AI
 *
 * @author Joel Bradley
 */
public final class AddAICommand implements ICommand {

    private AddAIDTO dto;

    /**
     * Communicates with the ServerFacade to carry out the Add AI command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() {
        return null;
    }

    @Override
    public void setParams(IDTO dto) {
        this.dto = (AddAIDTO)dto;
    }
}