package server.persistence.dao.daos.sql;

import com.google.gson.JsonObject;
import server.persistence.dao.daos.IGameDAO;

/**
 * Created by Kyle 'TMD' Cornelison on 4/2/2016.
 */
public class SQLGameDAO implements IGameDAO {
    SQLGameDAO instance = null;

    private SQLGameDAO() {
    }

    public SQLGameDAO getInstance(){
        if(instance == null){
            instance = new SQLGameDAO();
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
