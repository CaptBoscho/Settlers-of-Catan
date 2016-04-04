package server.persistence.dao.daos.redis;

import server.commands.ICommand;
import server.persistence.dao.daos.ICommandDAO;

import java.util.List;

/**
 * Created by Kyle 'TMD' Cornelison on 4/2/2016.
 */
public class RedisDBCommandDAO implements ICommandDAO {

    RedisDBCommandDAO instance = null;

    private RedisDBCommandDAO(){

    }

    public RedisDBCommandDAO getInstance(){
        if(instance == null){
            instance = new RedisDBCommandDAO();
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
