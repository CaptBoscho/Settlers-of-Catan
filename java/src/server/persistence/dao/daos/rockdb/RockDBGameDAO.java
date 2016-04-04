package server.persistence.dao.daos.rockdb;

import com.google.gson.JsonObject;
import server.persistence.dao.daos.IGameDAO;

/**
 * Created by Kyle 'TMD' Cornelison on 4/2/2016.
 */
public class RockDBGameDAO implements IGameDAO {

    RockDBGameDAO instance = null;

    private RockDBGameDAO(){

    }

    public RockDBGameDAO getInstance(){
        if (instance == null){
            instance = new RockDBGameDAO();
        }
        return instance;
    }

    @Override
    public void createGame(int id, String name, JsonObject blob) {

    }

    @Override
    public void updateGame(int id, JsonObject blob) {

    }

    @Override
    public JsonObject loadGame(int id) {
        return null;
    }
}
