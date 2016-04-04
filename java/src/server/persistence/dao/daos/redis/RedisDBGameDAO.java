package server.persistence.dao.daos.redis;

import com.google.gson.JsonObject;
import server.persistence.dao.daos.IGameDAO;

/**
 * Created by Kyle 'TMD' Cornelison on 4/2/2016.
 */
public class RedisDBGameDAO implements IGameDAO {

    RedisDBGameDAO instance = null;

    private RedisDBGameDAO(){

    }

    public RedisDBGameDAO getInstance(){
        if (instance == null){
            instance = new RedisDBGameDAO();
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
