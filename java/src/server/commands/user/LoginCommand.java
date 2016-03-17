package server.commands.user;

import server.commands.ICommand;
import shared.dto.GameModelDTO;

/**
 * A command object that logs a player in
 *
 * Created by Danny Harding on 3/9/16.
 */
public class LoginCommand implements ICommand {

    /**
     * Communicates with the ServerFacade to carry out the Login command
     *
     * @return GameModelDTO with information about the login
     */
    @Override
    public GameModelDTO execute() {
        return null;
    }
}
