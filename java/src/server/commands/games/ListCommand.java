package server.commands.games;

import server.commands.ICommand;
import shared.dto.GameModelDTO;

/**
 * A command object that lists the available games.
 *
 * Created by Danny Harding on 3/10/16.
 */
public class ListCommand implements ICommand {

    /**
     * Communicates with the ServerFacade to carry out the List command
     *
     * @return GameModelDTO with information about the games list
     */
    @Override
    public GameModelDTO execute() {
        return null;
    }
}
