package server.commands.moves;

import com.google.gson.JsonObject;
import server.commands.ICommand;
import shared.dto.GameModelDTO;

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
    public GameModelDTO execute() {
        return null;
    }

}
