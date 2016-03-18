package server.commands.moves;

import server.commands.ICommand;
import shared.dto.IDTO;

/**
 * A command object that plays a monopoly card
 *
 * @author Joel Bradley
 */
public class MonopolyCommand implements ICommand {

    /**
     * Constructor
     */
    public MonopolyCommand() {

    }

    /**
     * Communicates with the ServerFacade to carry out the Monopoly command
     * @return IDTO
     */
    @Override
    public IDTO execute() {
        return null;
    }

}
