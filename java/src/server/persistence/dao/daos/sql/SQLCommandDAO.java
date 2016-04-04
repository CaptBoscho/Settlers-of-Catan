package server.persistence.dao.daos.sql;

import server.commands.ICommand;
import server.persistence.dao.daos.ICommandDAO;

import java.util.List;

/**
 * Created by Kyle 'TMD' Cornelison on 4/2/2016.
 */
public class SQLCommandDAO implements ICommandDAO {

    SQLCommandDAO instance = null;

    private SQLCommandDAO(){

    }

    public SQLCommandDAO getInstance(){
        if (instance == null){
            instance = new SQLCommandDAO();
        }
        return instance;
    }

    @Override
    public void createCommand(int gameID, int version, ICommand command) {

    }

    @Override
    public void deleteGameCommands(int gameID) {

    }

    @Override
    public List<ICommand> getCommands(int gameID) {
        return null;
    }
    // TODO: 4/2/2016 Corbin will stub this out 
}
