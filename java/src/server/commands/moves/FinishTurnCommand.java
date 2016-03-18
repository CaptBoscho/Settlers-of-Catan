package server.commands.moves;

import server.commands.ICommand;
import shared.dto.IDTO;

/**
 * A command object that finishes a turn
 *
 * @author Joel Bradley
 */
public class FinishTurnCommand implements ICommand {

    /**
     * Constructor
     */
    public FinishTurnCommand() {

    }

    /**
     * Communicates with the ServerFacade to carry out the Finish Turn command
     * @return IDTO
     */
    @Override
    public IDTO execute() {
        return null;
    }

}
