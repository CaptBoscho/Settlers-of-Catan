package server.persistence.dao.daos;

import com.google.gson.JsonObject;

/**
 * Created by Kyle 'TMD' Cornelison on 4/2/2016.
 */
public interface IGameDAO{

    /**
     * Creates the game object in the database
     * @param id
     * @param name
     * @param blob
     */
    void createGame(int id, String name, JsonObject blob);

    /**
     * updates the gameblob for that game
     * @param id
     * @param blob
     */
    void updateGame(int id, JsonObject blob);

    /**
     * returns the json object that represents the
     * state of the game.
     * @param id
     * @return
     */
    JsonObject loadGame(int id);
}
