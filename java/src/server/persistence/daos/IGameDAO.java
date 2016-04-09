package daos;

import dto.GameDTO;
import dto.IDTO;
import exceptions.GameTableException;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Kyle 'TMD' Cornelison on 4/5/2016.
 */
public interface IGameDAO {
    /**
     * Handles adding a user,
     * adding a command
     * adding a game
     * @param dto
     */
    void addGameObject(GameDTO dto) throws GameTableException, SQLException;

    /**
     * Handles verifying user which returns userID
     * Getting the current game model
     * getting a list of Commands
     * @return
     */
    GameDTO getGameModel(int gameID) throws SQLException, GameTableException;

    List<GameDTO> getAllGames() throws SQLException;

    /**
     * mostly be used for updating the game blob state
     * @param dto
     */
    void updateGame(GameDTO dto) throws GameTableException, SQLException;

    /**
     * Deletes all games
     */
    void deleteAllGames() throws SQLException, GameTableException;

    /**
     * Deletes a game
     */
    void deleteGame(int gameID) throws GameTableException, SQLException;
}
