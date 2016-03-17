package server.commands.game;

import server.commands.ICommand;
import shared.dto.GameModelDTO;

/**
 * A command object that adds an AI
 *
 * @author Joel Bradley
 */
public class AddAICommand implements ICommand {

    /**
     * Constructor
     */
    public AddAICommand() {

    }

    /**
     * Communicates with the ServerFacade to carry out the Add AI command
     * @return GameModelDTO
     */
    @Override
    public GameModelDTO execute() {
        return null;
    }

}