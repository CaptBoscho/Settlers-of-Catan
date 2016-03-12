package server.commands.games;

import com.google.gson.JsonObject;
import server.commands.ICommand;

/**
 * A command object that creates a game.
 *
 * Created by Danny Harding on 3/10/16.
 */
public class CreateCommand implements ICommand {

    /**
     * Communicates with the ServerFacade to carry out the Create game command
     *
     * @return JsonObject with information about the game creation
     */
    @Override
    public JsonObject execute() {
        return null;
    }
}
