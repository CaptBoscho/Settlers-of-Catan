package server.factories;

import com.google.gson.JsonObject;
import server.commands.ICommand;
import server.commands.game.*;
import server.facade.IFacade;
import server.facade.ServerFacade;
import shared.dto.IDTO;

/**
 * A factory class that creates Game Commands on demand. Use this class to get a Game Command.
 * @author Derek Argueta
 */
public class GameCommandFactory {

    private IFacade facade;
    private static GameCommandFactory instance = null;

    private GameCommandFactory() {
        facade = ServerFacade.getInstance();
    }

    public static GameCommandFactory getInstance() {
        if(instance == null) {
            instance = new GameCommandFactory();
        }

        return instance;
    }

    public void bind(IFacade new_facade){
        facade = new_facade;
    }


    /**
     * Creates a game command based on a given string
     * @return an ICommand object
     */
    public ICommand createCommand(IDTO dto) {
        /*
       Need to if(object instanceof ...) for modeldto, addAIDTO, listAIDTO
         */
        return null;

    }

}
