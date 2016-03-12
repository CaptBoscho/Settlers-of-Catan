package server.commands.games;

import com.google.gson.JsonObject;
import server.commands.ICommand;
import server.facade.IFacade;

/**
 * A command object that lists the available games.
 *
 * Created by Danny Harding on 3/10/16.
 */
public class ListCommand implements ICommand {

    /**
     * Communicates with the ServerFacade to carry out the List command
     *
     * @return JsonObject with information about the games list
     */
    @Override
    public JsonObject execute(IFacade facade) {
        return null;
    }
}
