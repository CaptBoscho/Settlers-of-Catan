package server.commands.moves;

import com.google.gson.JsonObject;
import server.commands.ICommand;
import shared.dto.GameModelDTO;

/**
 * A command object that plays a monopoly card
 *
 * @author Joel Bradley
 */
public class MonopolyCommand implements ICommand {

    /**
     * Constructor
     */
    public MonopolyCommand() {

    }

    /**
     * Communicates with the ServerFacade to carry out the Monopoly command
     * @return JsonObject
     */
    @Override
    public GameModelDTO execute() {
        return null;
    }

}
