package server.commands.games;

import server.commands.ICommand;
import shared.dto.IDTO;

/**
 * A command object that adds a player to a game.
 *
 * Created by Danny Harding on 3/10/16.
 */
public class JoinCommand implements ICommand {

    /**
     * Communicates with the ServerFacade to carry out the Join Game command
     *
     * @return IDTO with information about the join
     */
    @Override
    public IDTO execute() {
        return null;
    }
}
