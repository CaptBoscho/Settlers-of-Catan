package server.commands.games;

import server.commands.ICommand;
import shared.dto.GameModelDTO;

/**
 * A command object that adds a player to a game.
 *
 * Created by Danny Harding on 3/10/16.
 */
public class JoinCommand implements ICommand {

    /**
     * Communicates with the ServerFacade to carry out the Join Game command
     *
     * @return GameModelDTO with information about the join
     */
    @Override
    public GameModelDTO execute() {
        return null;
    }
}
