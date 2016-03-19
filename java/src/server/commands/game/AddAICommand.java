package server.commands.game;

import server.commands.ICommand;
import shared.dto.IDTO;

/**
 * A command object that adds an AI
 *
 * @author Joel Bradley
 */
public class AddAICommand implements ICommand {

    /**
     * Constructor
     */
    public AddAICommand() {

    }

    /**
     * Communicates with the ServerFacade to carry out the Add AI command
     * @return IDTO
     */
    @Override
    public IDTO execute() {
        return null;
    }

}