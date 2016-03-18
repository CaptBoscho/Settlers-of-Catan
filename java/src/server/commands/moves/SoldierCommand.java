package server.commands.moves;

import server.commands.ICommand;
import shared.dto.IDTO;

/**
 * A command object that plays a soldier card
 *
 * @author Joel Bradley
 */
public class SoldierCommand implements ICommand {

    /**
     * Constructor
     */
    public SoldierCommand() {

    }

    /**
     * Communicates with the ServerFacade to carry out the Soldier command
     * @return IDTO
     */
    @Override
    public IDTO execute() {
        return null;
    }

}
