package server.commands.moves;

import server.commands.ICommand;
import shared.dto.GameModelDTO;

/**
 * A command object that discards cards
 *
 * @author Joel Bradley
 */
public class DiscardCardsCommand implements ICommand {

    /**
     * Constructor
     */
    public DiscardCardsCommand() {

    }

    /**
     * Communicates with the ServerFacade to carry out the Discard Cards command
     * @return GameModelDTO
     */
    @Override
    public GameModelDTO execute() {
        return null;
    }

}
