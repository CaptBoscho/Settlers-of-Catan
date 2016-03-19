package server.commands.game;

import server.commands.ICommand;
import shared.dto.GameModelDTO;

/**
 * A command object that gets the game model
 *
 * @author Joel Bradley
 */
public class ModelCommand implements ICommand {

    /**
     * Constructor
     */
    public ModelCommand() {

    }

    /**
     * Communicates with the ServerFacade to carry out the Model command
     * @return IDTO
     */
    @Override
    public GameModelDTO execute() {
        return null;
    }

}
