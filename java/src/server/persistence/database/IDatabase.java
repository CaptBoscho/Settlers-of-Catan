package server.persistence.database;

import server.persistence.dto.CommandDTO;
import server.persistence.dto.GameDTO;
import server.persistence.dto.UserDTO;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by Kyle 'TMD' Cornelison on 4/2/2016.
 */
public interface IDatabase {


    void loadJar(String str);
    //region Plugin Methods
    /**
     * Clears the database
     */
    void clear();

    List<GameDTO> getAllGames() throws InvocationTargetException, IllegalAccessException;

    void addGameObject(GameDTO dto) throws InvocationTargetException, IllegalAccessException;

    GameDTO getGameModel(int gameId) throws InvocationTargetException, IllegalAccessException;

    void updateGame(GameDTO dto) throws InvocationTargetException, IllegalAccessException;

    void deleteAllGames() throws InvocationTargetException, IllegalAccessException;

    void deleteGame(int gameId) throws InvocationTargetException, IllegalAccessException;

    void addCommand(CommandDTO dto) throws InvocationTargetException, IllegalAccessException;

    List<CommandDTO> getCommands(int gameId) throws InvocationTargetException, IllegalAccessException;

    List<CommandDTO> getAllCommands() throws InvocationTargetException, IllegalAccessException;

    void deleteAllCommands() throws InvocationTargetException, IllegalAccessException;

    void deleteCommandsFromGame(int gameId) throws InvocationTargetException, IllegalAccessException;

    void addUser(UserDTO dto) throws InvocationTargetException, IllegalAccessException;

    List<UserDTO> getUsers() throws InvocationTargetException, IllegalAccessException;

    void deleteUsers() throws InvocationTargetException, IllegalAccessException;

    //endregion
}
