package daos;

import dto.CommandDTO;
import dto.IDTO;
import exceptions.CommandTableException;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Kyle 'TMD' Cornelison on 4/5/2016.
 */
public interface ICommandDAO {
    /**
     * Handles adding a user,
     * adding a command
     * adding a game
     * @param dto
     */
    void addCommand(CommandDTO dto) throws CommandTableException, SQLException;

    /**
     * Handles verifying user which returns userID
     * Getting the current game model
     * getting a list of Commands
     * @return
     */
    List<CommandDTO> getCommands(int gameID) throws CommandTableException, SQLException;

    List<CommandDTO> getAllCommands() throws SQLException;

    /**
     * mostly be used for updating the game blob state
     */
    void deleteAllCommands() throws CommandTableException, SQLException;

    /**
     * Mostly be used for deleting commands every n
     * moves.
     */
    void deleteCommandsFromGame(int gameID) throws CommandTableException, SQLException;
}
