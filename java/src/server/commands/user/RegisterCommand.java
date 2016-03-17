package server.commands.user;

import server.commands.ICommand;
import shared.dto.GameModelDTO;

/**
 * A command object that registers a player.
 *
 * Created by Danny Harding on 3/10/16.
 */
public class RegisterCommand implements ICommand {

    /**
     * Communicates with the ServerFacade to carry out the Register command
     *
     * @return GameModelDTO with information about the registration
     */
    @Override
    public GameModelDTO execute() {
        return null;
    }
}
