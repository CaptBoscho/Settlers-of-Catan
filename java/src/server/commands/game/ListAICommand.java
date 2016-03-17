package server.commands.game;

import server.commands.ICommand;
import shared.dto.GameModelDTO;

/**
 * A command object that list the AI
 *
 * @author Joel Bradley
 */
public class ListAICommand implements ICommand {

    /**
     * Constructor
     */
    public ListAICommand() {

    }

    /**
     * Communicates with the ServerFacade to carry out the List AI command
     * @return GameModelDTO
     */
    @Override
    public GameModelDTO execute() {
        return null;
    }

}
