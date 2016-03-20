package server.commands.game;

import server.commands.ICommand;
import server.exceptions.AddAIException;
import server.exceptions.CommandExecutionFailedException;
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
     * Constructor
     */
    public AddAICommand(IFacade facade, AddAIDTO dto) {
        this.facade = facade;
        this.dto = dto;
    }

    /**
     * Communicates with the ServerFacade to carry out the Add AI command
     * @return IDTO
     */
    @Override
    public IDTO execute() throws CommandExecutionFailedException {
        try {
            return facade.addAI(1,dto.getAIType());
        } catch (AddAIException e) {
            e.printStackTrace();
            throw new CommandExecutionFailedException("Failed to add the AI Player!");
        }
    }
}