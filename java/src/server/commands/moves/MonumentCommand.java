package server.commands.moves;

import server.commands.ICommand;
import shared.dto.GameModelDTO;

/**
 * A command object that plays a monument card
 *
 * @author Joel Bradley
 */
public class MonumentCommand implements ICommand {

    /**
     * Constructor
     */
    public MonumentCommand() {

    }

    /**
     * Communicates with the ServerFacade to carry out the Monument command
     * @return GameModelDTO
     */
    @Override
    public GameModelDTO execute() {
        return null;
    }

}