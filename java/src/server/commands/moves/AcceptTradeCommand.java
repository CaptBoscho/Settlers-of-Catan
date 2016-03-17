package server.commands.moves;

import server.commands.ICommand;
import shared.dto.GameModelDTO;

/**
 * A command object that accepts a trade
 *
 * @author Joel Bradley
 */
public class AcceptTradeCommand implements ICommand {

    /**
     * Constructor
     */
    public AcceptTradeCommand() {

    }

    /**
     * Communicates with the ServerFacade to carry out the Accept Trade command
     * @return GameModelDTO
     */
    @Override
    public GameModelDTO execute() {
        return null;
    }

}