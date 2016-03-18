package server.commands.moves;

import com.google.gson.JsonObject;
import server.commands.ICommand;
import shared.dto.GameModelDTO;

/**
 * A command object that rolls a number
 *
 * @author Joel Bradley
 */
public class RollNumberCommand implements ICommand {

    /**
     * Constructor
     */
    public RollNumberCommand() {

    }

    /**
     * Communicates with the ServerFacade to carry out the Roll Number command
     * @return JsonObject
     */
    @Override
    public GameModelDTO execute() {
        return null;
    }

}
