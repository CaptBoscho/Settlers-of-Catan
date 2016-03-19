package server.commands.game;

import com.google.gson.JsonObject;
import server.commands.ICommand;
import shared.dto.IDTO;

/**
 * A command object that list the AI
 *
 * @author Joel Bradley
 */
public class ListAICommand implements ICommand {

    /**
     * Constructor
     */
    public ListAICommand() {

    }

    /**
     * Communicates with the ServerFacade to carry out the List AI command
     * @return JsonObject
     */
    @Override
    public IDTO execute() {
        return null;
    }

}
