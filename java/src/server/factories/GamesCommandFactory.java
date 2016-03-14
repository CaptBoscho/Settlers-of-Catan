package server.factories;

import server.commands.ICommand;
import server.commands.games.*;
import server.facade.IFacade;
import server.facade.ServerFacade;
import shared.dto.CreateGameDTO;
import shared.dto.JoinGameDTO;

/**
 * A factory class that creates Games Commands on demand.  Use this class to get a Games Command
 * Created by Danny Harding on 3/10/16.
 */
public class GamesCommandFactory {

    private IFacade facade;
    private static GamesCommandFactory instance = null;

    private GamesCommandFactory() {
        facade = new ServerFacade();
    }

    public static GamesCommandFactory getInstance() {
        if (instance == null) {
            instance = new GamesCommandFactory();
        }

        return instance;
    }

    public void bind(IFacade new_facade){
        facade = new_facade;
    }

    /**
     * Creates a Games command based on a given string
     * @return an ICommand object
     */
    public ICommand createCommand(Object obj) {
        if(obj instanceof CreateGameDTO){

        }else if(obj instanceof JoinGameDTO){

        }//else if(obj instanceof ListGamesDTO){}

        return null;
    }
}
