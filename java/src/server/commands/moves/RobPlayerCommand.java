package server.commands.moves;

import server.commands.ICommand;
import shared.dto.GameModelDTO;

/**
 * A command object that robs a player
 *
 * @author Joel Bradley
 */
public class RobPlayerCommand implements ICommand {

    /**
     * Constructor
     */
    public RobPlayerCommand() {

    }

    /**
     * Communicates with the ServerFacade to carry out the Rob Player command
     * @return GameModelDTO
     */
    @Override
    public GameModelDTO execute() {
        return null;
    }

}
