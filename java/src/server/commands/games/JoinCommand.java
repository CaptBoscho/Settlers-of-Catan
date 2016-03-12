package server.commands.games;

import com.google.gson.JsonObject;
import server.commands.ICommand;
import server.facade.IFacade;

/**
 * A command object that adds a player to a game.
 *
 * Created by Danny Harding on 3/10/16.
 */
public class JoinCommand implements ICommand {

    /**
     * Communicates with the ServerFacade to carry out the Join Game command
     *
     * @return JsonObject with information about the join
     */
    @Override
    public JsonObject execute(IFacade facade) {
        return null;
    }
}
