package server.factories;

import server.commands.ICommand;
import server.commands.game.AddAICommand;
import server.commands.game.ListAICommand;
import server.facade.IFacade;
import server.facade.ServerFacade;
import shared.dto.AddAIDTO;
import shared.dto.GameModelDTO;
import shared.dto.IDTO;
import shared.dto.ListAIDTO;

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

    public void bind(IFacade newFacade){
        facade = newFacade;
    }


    /**
     * Creates a game command based on a given string
     * @return an ICommand object
     */
    public ICommand createCommand(IDTO dto) {
       if(dto instanceof GameModelDTO){
           return null;
       }else if(dto instanceof ListAIDTO){
           return new ListAICommand(facade);
       }else if(dto instanceof AddAIDTO){
           return new AddAICommand(facade, (AddAIDTO)dto);
       }else{
           return null;
       }
    }

}
