package server.commands.user;

import com.google.gson.JsonObject;
import server.commands.ICommand;

/**
 * A command object that registers a player.
 *
 * Created by Danny Harding on 3/10/16.
 */
public class RegisterCommand implements ICommand {

    /**
     * Communicates with the ServerFacade to carry out the Register command
     *
     * @return JsonObject with information about the registration
     */
    @Override
    public JsonObject execute() {
        return null;
    }
}
