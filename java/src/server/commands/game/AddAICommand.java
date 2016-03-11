package server.commands.game;

import com.google.gson.JsonObject;
import server.commands.ICommand;

/**
 * A command object that adds an AI
 *
 * @author Joel Bradley
 */
public class AddAICommand implements ICommand {

    /**
     * Constructor
     */
    public AddAICommand() {

    }

    /**
     * Communicates with the ServerFacade to carry out the Add AI command
     * @return JsonObject
     */
    @Override
    public JsonObject execute() {
        return null;
    }

}