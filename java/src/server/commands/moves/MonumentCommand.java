package server.commands.moves;

import com.google.gson.JsonObject;
import server.commands.ICommand;

/**
 * A command object that plays a monument card
 *
 * @author Joel Bradley
 */
public class MonumentCommand implements ICommand {

    /**
     * Constructor
     */
    public MonumentCommand() {

    }

    /**
     * Communicates with the ServerFacade to carry out the Monument command
     * @return JsonObject
     */
    @Override
    public JsonObject execute() {
        return null;
    }

}