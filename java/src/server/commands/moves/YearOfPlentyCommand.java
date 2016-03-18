package server.commands.moves;

import server.commands.ICommand;
import shared.dto.IDTO;

/**
 * A command object that plays a year of plenty card
 *
 * @author Joel Bradley
 */
public class YearOfPlentyCommand implements ICommand {

    /**
     * Constructor
     */
    public YearOfPlentyCommand() {

    }

    /**
     * Communicates with the ServerFacade to carry out the YearOfPlenty command
     * @return IDTO
     */
    @Override
    public IDTO execute() {
        return null;
    }

}
