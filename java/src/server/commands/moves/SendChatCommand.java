package server.commands.moves;

import com.google.gson.JsonObject;
import server.commands.ICommand;
import server.facade.IFacade;

/**
 * A command object that sends a chat
 *
 * @author Joel Bradley
 */
public class SendChatCommand implements ICommand {

    /**
     * Constructor
     */
    public SendChatCommand() {

    }

    /**
     * Communicates with the ServerFacade to carry out the Send Chat command
     * @return JsonObject
     */
    @Override
    public JsonObject execute(IFacade facade) {
        return null;
    }

}
