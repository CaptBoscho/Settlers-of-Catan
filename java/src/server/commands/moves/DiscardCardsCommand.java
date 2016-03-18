package server.commands.moves;

import server.commands.ICommand;
import shared.dto.IDTO;

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
     * @return IDTO
     */
    @Override
    public IDTO execute() {
        return null;
    }

}
