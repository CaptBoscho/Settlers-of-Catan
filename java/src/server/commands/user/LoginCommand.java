package server.commands.user;

import server.commands.ICommand;
import shared.dto.IDTO;

/**
 * A command object that logs a player in
 *
 * Created by Danny Harding on 3/9/16.
 */
public class LoginCommand implements ICommand {

    /**
     * Communicates with the ServerFacade to carry out the Login command
     *
     * @return IDTO with information about the login
     */
    @Override
    public IDTO execute() {
        return null;
    }
}
