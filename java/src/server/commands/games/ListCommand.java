package server.commands.games;

import server.commands.ICommand;
import shared.dto.IDTO;

/**
 * A command object that lists the available games.
 *
 * Created by Danny Harding on 3/10/16.
 */
public class ListCommand implements ICommand {

    /**
     * Communicates with the ServerFacade to carry out the List command
     *
     * @return IDTO with information about the games list
     */
    @Override
    public IDTO execute() {
        return null;
    }
}
