package server.commands.moves;

import com.google.gson.JsonObject;
import server.commands.ICommand;
import shared.dto.GameModelDTO;

/**
 * A command object that discards cards
 *
 * @author Joel Bradley
 */
public class DiscardCardsCommand implements ICommand {

    /**
     * Constructor
     */
    public DiscardCardsCommand() {

    }

    /**
     * Communicates with the ServerFacade to carry out the Discard Cards command
     * @return JsonObject
     */
    @Override
    public GameModelDTO execute() {
        return null;
    }

}
