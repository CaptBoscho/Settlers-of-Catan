package server.persistence.dao.daos.rockdb;

import server.commands.ICommand;
import server.persistence.dao.daos.ICommandDAO;

import java.util.List;

/**
 * Created by Kyle 'TMD' Cornelison on 4/2/2016.
 */
public class RockDBCommandDAO implements ICommandDAO {

    RockDBCommandDAO instance = null;

    private RockDBCommandDAO(){

    }

    public RockDBCommandDAO getInstance(){
        if(instance == null){
            instance = new RockDBCommandDAO();
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
}
