package server.commands.game;

import com.google.gson.JsonObject;
import server.commands.ICommand;
import server.facade.IFacade;

/**
 * A command object that gets the game model
 *
 * @author Joel Bradley
 */
public class ModelCommand implements ICommand {

    /**
     * Constructor
     */
    public ModelCommand() {

    }

    /**
     * Communicates with the ServerFacade to carry out the Model command
     * @return JsonObject
     */
    @Override
    public JsonObject execute(IFacade facade) {
        return null;
    }

}
