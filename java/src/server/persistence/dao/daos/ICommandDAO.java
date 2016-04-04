package server.persistence.dao.daos;

import server.commands.ICommand;

import java.util.List;

/**
 * Created by Kyle 'TMD' Cornelison on 4/2/2016.
 */
public interface ICommandDAO {

    /**
     * Adds a command to the Database, version is the version
     * as specified in the game.
     * @param gameID
     * @param version
     * @param command
     */
    void createCommand(int gameID, int version, ICommand command);

    /**
     * Deletes all Commands that have the same gameID
     * @param gameID
     */
    void deleteGameCommands(int gameID);

    /**
     * Gets all the commands for the game so that you can reload the
     * game.
     * @param gameID
     * @return
     */
    List<ICommand> getCommands(int gameID);

}
