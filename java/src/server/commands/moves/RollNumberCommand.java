package server.commands.moves;

import server.commands.ICommand;
import shared.dto.IDTO;

/**
 * A command object that rolls a number
 *
 * @author Joel Bradley
 */
public class RollNumberCommand implements ICommand {

    /**
     * Constructor
     */
    public RollNumberCommand() {

    }

    /**
     * Communicates with the ServerFacade to carry out the Roll Number command
     * @return IDTO
     */
    @Override
    public IDTO execute() {
        return null;
    }

}
