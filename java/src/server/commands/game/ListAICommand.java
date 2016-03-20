package server.commands.game;

import client.services.CommandExecutionFailed;
import server.commands.ICommand;
import server.exceptions.CommandExecutionFailedException;
import server.exceptions.ListAIException;
import server.facade.IFacade;
import shared.dto.IDTO;
import shared.dto.ListAIDTO;

/**
 * A command object that list the AI
 *
 * @author Joel Bradley
 */
public class ListAICommand implements ICommand {
    private IFacade facade;

    /**
     * Constructor
     */
    public ListAICommand(IFacade facade) {
        this.facade = facade;
    }

    /**
     * Communicates with the ServerFacade to carry out the List AI command
     * @return IDTO
     */
    @Override
    public IDTO execute() throws CommandExecutionFailedException {
        try {
            return facade.listAI(1);
        } catch (ListAIException e) {
            e.printStackTrace();
            throw new CommandExecutionFailedException("Failed to list AI types!");
        }
    }

}
