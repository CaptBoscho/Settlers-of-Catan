package server.commands.moves;

import server.commands.ICommand;
import shared.dto.GameModelDTO;

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
     * @return GameModelDTO
     */
    @Override
    public GameModelDTO execute() {
        return null;
    }

}
