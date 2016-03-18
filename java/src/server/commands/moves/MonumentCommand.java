package server.commands.moves;

import com.google.gson.JsonObject;
import server.commands.ICommand;
import shared.dto.GameModelDTO;
import shared.model.game.Game;

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
    public GameModelDTO execute() {
        return null;
    }

}