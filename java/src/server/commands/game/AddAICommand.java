package server.commands.game;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.facade.IFacade;
import shared.dto.AddAIDTO;
import shared.dto.IDTO;

/**
 * A command object that adds an AI
 *
 * @author Joel Bradley
 */
public class AddAICommand implements ICommand {
    private IFacade facade;
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

    }
}