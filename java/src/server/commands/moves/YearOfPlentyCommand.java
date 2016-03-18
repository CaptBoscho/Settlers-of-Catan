package server.commands.moves;

import com.google.gson.JsonObject;
import server.commands.ICommand;
import shared.dto.GameModelDTO;

/**
 * A command object that plays a year of plenty card
 *
 * @author Joel Bradley
 */
public class YearOfPlentyCommand implements ICommand {

    /**
     * Constructor
     */
    public YearOfPlentyCommand() {

    }

    /**
     * Communicates with the ServerFacade to carry out the YearOfPlenty command
     * @return JsonObject
     */
    @Override
    public GameModelDTO execute() {
        return null;
    }

}
