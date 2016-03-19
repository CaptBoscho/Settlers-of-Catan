package server.commands.games;

import server.commands.ICommand;
import shared.dto.IDTO;

/**
 * A command object that creates a game.
 *
 * Created by Danny Harding on 3/10/16.
 */
public class CreateCommand implements ICommand {

    /**
     * Communicates with the ServerFacade to carry out the Create game command
     *
     * @return IDTO with information about the game creation
     */
    @Override
    public IDTO execute() {
        return null;
    }
}
