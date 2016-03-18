package server.commands.moves;

import server.commands.ICommand;
import shared.dto.IDTO;

/**
 * A command object that buys a development card
 *
 * @author Joel Bradley
 */
public class BuyDevCardCommand implements ICommand {

    /**
     * Constructor
     */
    public BuyDevCardCommand() {

    }

    /**
     * Communicates with the ServerFacade to carry out the Buy Development Card command
     * @return IDTO
     */
    @Override
    public IDTO execute() {
        return null;
    }

}
