package server.commands.games;

import server.commands.ICommand;
import shared.dto.GameModelDTO;

/**
 * A command object that creates a game.
 *
 * Created by Danny Harding on 3/10/16.
 */
public class CreateCommand implements ICommand {

    /**
     * Communicates with the ServerFacade to carry out the Create game command
     *
     * @return GameModelDTO with information about the game creation
     */
    @Override
    public GameModelDTO execute() {
        return null;
    }
}
