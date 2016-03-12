package server.commands.user;

import com.google.gson.JsonObject;
import server.commands.ICommand;
import server.facade.IFacade;

/**
 * A command object that logs a player in
 *
 * Created by Danny Harding on 3/9/16.
 */
public class LoginCommand implements ICommand {

    /**
     * Communicates with the ServerFacade to carry out the Login command
     *
     * @return JsonObject with information about the login
     */
    @Override
    public JsonObject execute(IFacade facade) {
        return null;
    }
}
